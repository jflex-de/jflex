#!/bin/bash
#
# Copyright 2026, Gerwin Klein
# SPDX-License-Identifier: BSD-3-Clause
#
# Temporary coverage guard: checks for every Bazel test target whether Maven
# (surefire) runs the same test class.
#
# Usage: scripts/check-bazel-maven-parity.sh [--strict] [--verbose] [--targets FILE]
#
#   --strict        exit 1 if any Bazel java_test is not covered by Maven
#   --verbose       also list the covered and the non-Java targets
#   --targets FILE  read the Bazel test targets from FILE instead of running
#                   bazel query. FILE holds the output of
#                     bazel query 'tests(//... - //jflex/target/...)' --output=label_kind
#                   (or --output=label).
#
# Run Maven first (e.g. ./mvnw install); this script only reads the
# **/target/surefire-reports/TEST-*.xml files that are already there.
# Run ./mvnw clean first to avoid picking up stale reports.
#
# sh_test targets (the check_deps "deps_to_bootstrap_jflex_test" checks) have
# no Java class. They are listed separately and do not fail --strict.

set -e
set -o pipefail
# sort and join must agree on the collation.
export LC_ALL=C

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
cd "$BASEDIR"

STRICT=
VERBOSE=
TARGETS_FILE=
while [[ $# -gt 0 ]]; do
  case "$1" in
    --strict) STRICT=1 ;;
    --verbose) VERBOSE=1 ;;
    --targets)
      shift
      [[ $# -gt 0 ]] || { echo "--targets needs a file" >&2; exit 2; }
      TARGETS_FILE="$1"
      ;;
    -h|--help)
      awk 'NR >= 6 && /^#/ { sub(/^# ?/, ""); print; next } NR >= 6 { exit }' "$0"
      exit 0
      ;;
    *) echo "Unknown argument: $1 (try --help)" >&2; exit 2 ;;
  esac
  shift
done

WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

# 1. Bazel test targets, as "<kind> <package> <name>" lines.
if [[ -n "$TARGETS_FILE" ]]; then
  cp "$TARGETS_FILE" "$WORK/query.txt"
else
  if ! command -v bazel > /dev/null; then
    echo "bazel not found; install Bazelisk or pass --targets FILE" >&2
    exit 2
  fi
  bazel query 'tests(//... - //jflex/target/...)' --output=label_kind \
    2> "$WORK/query.err" > "$WORK/query.txt" || {
      cat "$WORK/query.err" >&2
      exit 2
    }
fi
# Accept "java_test rule //pkg:name" and plain "//pkg:name" lines.
awk '
  /^[[:space:]]*$/ { next }
  { label = $NF; kind = (NF >= 3) ? $1 : "?" }
  label !~ /^\/\/[^:]*:/ { print "Cannot parse target line: " $0 > "/dev/stderr"; exit 2 }
  {
    sub(/^\/\//, "", label)
    pkg = label; sub(/:.*/, "", pkg)
    name = label; sub(/^[^:]*:/, "", name)
    if (("/" pkg "/") ~ /\/target\//) next   # Maven copies of BUILD files
    print kind, pkg, name
  }' "$WORK/query.txt" | sort -u > "$WORK/targets.txt"

# 2. Map each target to a fully qualified class name.
#    Output lines: "<class> <package> <name>" (java), or into non_java/unmapped.
: > "$WORK/java.txt"
: > "$WORK/non_java.txt"
: > "$WORK/unmapped.txt"
while read -r kind pkg name; do
  if [[ "$kind" == "sh_test" ]]; then
    echo "//$pkg:$name" >> "$WORK/non_java.txt"
    continue
  fi
  srcs="$(find "$pkg" -name "$name.java" -type f 2> /dev/null)"
  count="$(printf '%s' "$srcs" | grep -c . || true)"
  if [[ "$count" -ne 1 ]]; then
    if [[ "$kind" == "?" && "$count" -eq 0 ]]; then
      # Unknown kind (plain label list) and no Java source: not a Java test.
      echo "//$pkg:$name" >> "$WORK/non_java.txt"
    else
      echo "//$pkg:$name ($count source files named $name.java)" >> "$WORK/unmapped.txt"
    fi
    continue
  fi
  jpkg="$(sed -n 's/^[[:space:]]*package[[:space:]]\{1,\}\([A-Za-z0-9_.]\{1,\}\)[[:space:]]*;.*/\1/p' "$srcs" | head -1)"
  echo "${jpkg:+$jpkg.}$name $pkg $name" >> "$WORK/java.txt"
done < "$WORK/targets.txt"
sort -o "$WORK/java.txt" "$WORK/java.txt"

# 3. Classes that Maven ran: testsuite name, total and skipped count per report.
find . \( -name .git -o -name 'bazel-*' -o -name node_modules \) -prune -o \
  -path '*/target/surefire-reports/TEST-*.xml' -type f -print > "$WORK/reports.txt"
REPORTS="$(grep -c . "$WORK/reports.txt" || true)"
: > "$WORK/ran.txt"
: > "$WORK/skipped.txt"
if [[ "$REPORTS" -gt 0 ]]; then
  tr '\n' '\0' < "$WORK/reports.txt" | xargs -0 awk '
    FNR == 1 { done = 0 }
    !done && /<testsuite[[:space:]>]/ {
      line = $0
      name = attr(line, "name"); tests = attr(line, "tests"); skipped = attr(line, "skipped")
      if (name == "") { name = FILENAME; sub(/.*\/TEST-/, "", name); sub(/\.xml$/, "", name) }
      print name, (tests == "" ? 1 : tests), (skipped == "" ? 0 : skipped)
      done = 1
    }
    function attr(s, a,   m) {
      if (match(s, "[[:space:]]" a "=\"[^\"]*\"")) {
        m = substr(s, RSTART, RLENGTH); sub(/^[^"]*"/, "", m); sub(/"$/, "", m); return m
      }
      return ""
    }' > "$WORK/suites.txt"
  # A class may be run by several modules; covered if any run was not all-skipped.
  awk '{ ran[$1] += ($2 - $3 > 0) ? 1 : 0; seen[$1] = 1 }
       END { for (c in seen) print c, (ran[c] > 0 ? "ran" : "skipped") }' "$WORK/suites.txt" \
    | sort > "$WORK/maven.txt"
  awk '$2 == "ran" { print $1 }' "$WORK/maven.txt" > "$WORK/ran.txt"
  awk '$2 == "skipped" { print $1 }' "$WORK/maven.txt" > "$WORK/skipped.txt"
fi

# 4. Compare.
join -j 1 "$WORK/java.txt" <(sort -u "$WORK/ran.txt") > "$WORK/covered.txt" || true
join -v 1 -j 1 "$WORK/java.txt" <(sort -u "$WORK/ran.txt") > "$WORK/missing.txt" || true
join -j 1 "$WORK/missing.txt" <(sort -u "$WORK/skipped.txt") | awk '{ print $1 }' \
  > "$WORK/missing_skipped.txt" || true
awk '{ print $1 }' "$WORK/java.txt" | uniq -d > "$WORK/dup_classes.txt"

n() { grep -c . "$1" || true; }
N_TARGETS="$(n "$WORK/targets.txt")"
N_JAVA="$(n "$WORK/java.txt")"
N_COVERED="$(n "$WORK/covered.txt")"
N_MISSING="$(n "$WORK/missing.txt")"
N_UNMAPPED="$(n "$WORK/unmapped.txt")"
N_NONJAVA="$(n "$WORK/non_java.txt")"
N_MISSING_TOTAL=$((N_MISSING + N_UNMAPPED))

# Area of a Bazel package, for the summary table.
area() {
  awk '{
    p = $0
    if (p ~ /^jflex\/src\/test\//) a = "jflex/src/test"
    else if (p ~ /^jflex\/examples\//) a = "jflex/examples"
    else if (p ~ /^javatests\/de\/jflex\/testcase\/unicode(\/|$)/) a = "javatests/de/jflex/testcase/unicode"
    else if (p ~ /^javatests\/de\/jflex\/testcase(\/|$)/) a = "javatests/de/jflex/testcase"
    else if (p ~ /^(java|javatests)\/de\/jflex\/[^\/]+/) { n = split(p, c, "/"); a = c[1] "/" c[2] "/" c[3] "/" c[4] }
    else { n = split(p, c, "/"); a = c[1] (n > 1 ? "/" c[2] : "") }
    print a
  }'
}

echo "## Bazel/Maven test parity"
echo
echo "Bazel test targets: $N_TARGETS ($N_JAVA Java tests, $N_NONJAVA non-Java)."
echo "Maven surefire reports read: $REPORTS."
echo
echo "| Area | Bazel Java tests | Covered by Maven | Missing |"
echo "|---|---:|---:|---:|"
{
  awk '{ print $2, "c" }' "$WORK/covered.txt"
  awk '{ print $2, "m" }' "$WORK/missing.txt"
  sed 's|^//\([^:]*\):.*|\1 m|' "$WORK/unmapped.txt"
} | while read -r pkg flag; do echo "$(echo "$pkg" | area) $flag"; done \
  | awk '{ t[$1]++; if ($2 == "c") c[$1]++; else m[$1]++ }
         END { for (a in t) printf "| %s | %d | %d | %d |\n", a, t[a], c[a], m[a] }' \
  | sort
echo "| **total** | **$((N_JAVA + N_UNMAPPED))** | **$N_COVERED** | **$N_MISSING_TOTAL** |"

if [[ "$REPORTS" -eq 0 ]]; then
  echo
  echo "**Warning:** no surefire reports found. Run Maven first, e.g. \`./mvnw install\`."
fi

if [[ -s "$WORK/dup_classes.txt" ]]; then
  echo
  echo "**Warning:** several Bazel targets map to the same class:"
  echo
  sed 's/^/- /' "$WORK/dup_classes.txt"
fi

if [[ "$N_UNMAPPED" -gt 0 ]]; then
  echo
  echo "### Bazel Java tests without a unique source file ($N_UNMAPPED)"
  echo
  sed 's/^/- /' "$WORK/unmapped.txt"
fi

if [[ "$N_MISSING" -gt 0 ]]; then
  echo
  echo "### Not run by Maven ($N_MISSING)"
  echo
  awk '{ printf "- `%s` (//%s:%s)\n", $1, $2, $3 }' "$WORK/missing.txt"
  if [[ -s "$WORK/missing_skipped.txt" ]]; then
    echo
    echo "Of these, Maven has reports with all tests skipped for:"
    echo
    sed 's/^/- /' "$WORK/missing_skipped.txt"
  fi
fi

if [[ "$VERBOSE" ]]; then
  echo
  echo "### Covered by Maven ($N_COVERED)"
  echo
  awk '{ printf "- `%s` (//%s:%s)\n", $1, $2, $3 }' "$WORK/covered.txt"
fi

if [[ "$N_NONJAVA" -gt 0 ]]; then
  echo
  echo "### Non-Java Bazel tests ($N_NONJAVA)"
  echo
  echo "No Java class to compare; these do not count as missing."
  if [[ "$VERBOSE" ]]; then
    echo
    sed 's/^/- /' "$WORK/non_java.txt"
  else
    echo "Names: $(sed 's|^//[^:]*:||' "$WORK/non_java.txt" | sort | uniq -c \
      | awk '{ printf "%s%s (%d)", sep, $2, $1; sep = ", " }'). Use --verbose to list them."
  fi
fi

if [[ "$STRICT" && "$N_MISSING_TOTAL" -gt 0 ]]; then
  echo
  echo "FAILED: $N_MISSING_TOTAL Bazel Java tests are not covered by Maven." >&2
  exit 1
fi
