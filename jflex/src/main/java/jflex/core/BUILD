load("@jflex_rules//cup:cup.bzl", "cup")
load("@jflex_rules//jflex:jflex.bzl", "jflex")

package(default_visibility = ["//visibility:public"])

SRCS_DEPRECATED_DFA = [
    "DeprecatedDfa.java",
    "StatePairList.java",
]

java_library(
    name = "core",
    srcs = glob(
        ["*.java"],
        exclude = SRCS_DEPRECATED_DFA,
    ) + [
        ":gen_parser",
        ":gen_scanner",
    ],
    visibility = ["//jflex:visibility"],
    deps = [
        "//cup:cup_runtime",
        "//jflex/src/main/java/jflex/base",
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core/unicode",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/io",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/logging",
        "//jflex/src/main/java/jflex/option",
        "//jflex/src/main/java/jflex/performance",
        "//jflex/src/main/java/jflex/scanner",
        "//jflex/src/main/java/jflex/state",
    ],
)

# Use bootstrap version defined in @jflex_rules
jflex(
    name = "gen_scanner",
    srcs = ["//jflex/src/main/jflex:LexScan.flex"],
    outputs = ["LexScan.java"],
    skeleton = "//jflex/src/main/jflex:skeleton.nested",
)

cup(
    name = "gen_parser",
    src = "//jflex/src/main/cup:LexParse.cup",
    cup_bin = "@jflex_rules//cup:cup_bin",
    interface = True,
    parser = "LexParse",
    symbols = "sym",
)

java_library(
    name = "deprecated_dfa",
    testonly = True,
    srcs = SRCS_DEPRECATED_DFA,
    deps = [
        ":core",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/logging",
        "//jflex/src/main/java/jflex/option",
    ],
)
