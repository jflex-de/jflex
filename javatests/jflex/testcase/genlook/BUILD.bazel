# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

# genlook

jflex(
    name = "gen_genlook_scanner",
    srcs = ["genlook.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Genlook.java"],
)

java_library(
    name = "genlook_scanner",
    srcs = [
        ":gen_genlook_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "GenlookGoldenTest",
    srcs = [
        "GenlookGoldenTest.java",
    ],
    data = [
        "genlook-0.input",
        "genlook-0.output",
    ],
    deps = [
        ":genlook_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
