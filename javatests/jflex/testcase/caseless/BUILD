load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_caseless_jflex_scanner",
    srcs = ["caseless.jflex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["CaselessScanner.java"],
)

java_test(
    name = "CaselessJflexTest",
    srcs = [
        "CaselessJflexTest.java",
        "State.java",
        ":gen_caseless_jflex_scanner",
    ],
    deps = [
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
