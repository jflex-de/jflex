# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

# line

jflex(
    name = "gen_line_scanner",
    srcs = ["line.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Line.java"],
)

java_library(
    name = "line_scanner",
    srcs = [
        ":gen_line_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "LineGoldenTest",
    srcs = [
        "LineGoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":line_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
