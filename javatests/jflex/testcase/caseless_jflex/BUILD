# caseless

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_caseless_scanner",
    srcs = ["caseless.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Caseless.java"],
)

java_library(
    name = "caseless_scanner",
    srcs = [
        ":gen_caseless_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden:model",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "CaselessGoldenTest",
    srcs = [
        "CaselessGoldenTest.java",
    ],
    data = [
        "caseless-0.input",
        "caseless-0.output",
    ],
    deps = [
        ":caseless_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/testing/testsuite/golden:model",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
