# caseless

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
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "CaselessJflexTest",
    srcs = [
        "CaselessJflexTest.java",
        "State.java",
    ],
    deps = [
        ":caseless_scanner",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
