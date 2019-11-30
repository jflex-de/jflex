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
    name = "Ccl2GoldenTest",
    srcs = [
        "Ccl3GoldenTest.java",
    ],
    data = [
        "ccl3-0.input",
        "ccl3-0.output",
    ],
    deps = [
        ":ccl_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)

java_test(
    name = "Ccl3GoldenTest",
    srcs = [
        "Ccl3GoldenTest.java",
    ],
    data = [
        "ccl3-0.input",
        "ccl3-0.output",
    ],
    deps = [
        ":ccl_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
