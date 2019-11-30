load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_eof_scanner",
    srcs = ["eof.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Scanner.java"],
)

java_library(
    name = "eof_scanner",
    srcs = [
        "Token.java",
        ":gen_eof_scanner",
    ],
    deps = [
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "EofTest",
    srcs = [
        "EofTest.java",
    ],
    deps = [
        ":eof_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
