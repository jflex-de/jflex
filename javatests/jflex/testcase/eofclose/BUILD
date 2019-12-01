# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

# eofclose

jflex(
    name = "gen_eofclose_scanner",
    srcs = ["eofclose.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Eofclose.java"],
)

java_library(
    name = "eofclose_scanner",
    srcs = [
        ":gen_eofclose_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "EofCloseTest",
    srcs = [
        "EofCloseTest.java",
    ],
    data = [
        "eofclose-0.input",
        "eofclose-0.output",
    ],
    deps = [
        ":eofclose_scanner",
        "//java/jflex/testing/assertion",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
