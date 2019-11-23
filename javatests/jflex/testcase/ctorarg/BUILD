# ctorarg

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_ctorarg_scanner",
    srcs = ["ctorarg.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Ctorarg.java"],
)

java_library(
    name = "ctorarg_scanner",
    srcs = [
        ":gen_ctorarg_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "CtorargGoldenTest",
    srcs = [
        "CtorargGoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":ctorarg_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
