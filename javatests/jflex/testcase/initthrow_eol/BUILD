# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

# initthrow

jflex(
    name = "gen_initthrow_scanner",
    srcs = ["initthrow.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Initthrow.java"],
)

java_library(
    name = "initthrow_scanner",
    srcs = [
        ":gen_initthrow_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "InitthrowGoldenTest",
    srcs = [
        "InitthrowGoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":initthrow_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
