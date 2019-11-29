# ctorarg

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")
load("//testsuite:testsuite.bzl", "jflex_testsuite")

jflex(
    name = "gen_ctorarg_scanner",
    srcs = ["ctor-arg.flex"],
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
    name = "CtorArgTest",
    srcs = [
        "CtorArgTest.java",
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

jflex_testsuite(
    name = "CtorBadArgTest",
    srcs = ["CtorBadArgTest.java"],
    data = [
        "ctor-bad-arg.flex",
        "ctor-bad-arg.output",
    ],
    deps = [
        "//java/jflex/util/javac",
        "//jflex/src/main/java/jflex/exceptions",
        "//third_party/com/google/guava",
    ],
)
