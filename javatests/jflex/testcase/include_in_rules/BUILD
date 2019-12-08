# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

# IncludeInRules

jflex(
    name = "gen_IncludeInRules_scanner",
    srcs = ["IncludeInRules.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Includeinrules.java"],
)

java_library(
    name = "IncludeInRules_scanner",
    srcs = [
        ":gen_IncludeInRules_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "IncludeinrulesGoldenTest",
    srcs = [
        "IncludeinrulesGoldenTest.java",
    ],
    data = [
        "IncludeInRules-0.input",
        "IncludeInRules-0.output",
    ],
    deps = [
        ":IncludeInRules_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
