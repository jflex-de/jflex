# eolcomment

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_eolcomment_scanner",
    srcs = ["eolcomment.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Eolcomment.java"],
)

java_library(
    name = "eolcomment_scanner",
    srcs = [
        ":gen_eolcomment_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "EolcommentGoldenTest",
    srcs = [
        "EolcommentGoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":eolcomment_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
