# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")
load("//scripts:check_deps.bzl", "check_deps")

check_deps(
    name = "deps_to_bootstrap_jflex_test",
    prohibited = "@jflex_rules//jflex:jflex_bin",
)

# negation

jflex(
    name = "gen_negation_scanner",
    srcs = ["negation.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Negation.java"],
)

java_library(
    name = "negation_scanner",
    srcs = [
        ":gen_negation_scanner",
    ],
    deps = [
        "//java/de/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "NegationGoldenTest",
    srcs = [
        "NegationGoldenTest.java",
    ],
    data = [
        "negation-0.input",
        "negation-0.output",
        "negation-1.input",
        "negation-1.output",
    ],
    deps = [
        ":negation_scanner",
        "//java/de/jflex/testing/diff",
        "//java/de/jflex/testing/testsuite/golden",
        "//java/de/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
