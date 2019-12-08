# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

# include

jflex(
    name = "gen_include_scanner",
    srcs = ["include.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Include.java"],
)

java_library(
    name = "include_scanner",
    srcs = [
        ":gen_include_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "IncludeGoldenTest",
    srcs = [
        "IncludeGoldenTest.java",
    ],
    data = [
        "include-0.input",
        "include-0.output",
    ],
    deps = [
        ":include_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
