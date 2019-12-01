# ccl3

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_ccl_scanner",
    srcs = ["ccl.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Ccl.java"],
)

java_library(
    name = "ccl_scanner",
    srcs = [
        ":gen_ccl_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "CclPreGoldenTest",
    srcs = [
        "CclPreGoldenTest.java",
    ],
    data = [
        "ccl-0.input",
        "ccl-0.output",
    ],
    deps = [
        ":ccl_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
