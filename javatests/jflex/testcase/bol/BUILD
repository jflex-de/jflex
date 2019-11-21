load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "bol_scanner",
    srcs = ["bol.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["BolScanner.java"],
)

java_test(
    name = "BolTest",
    srcs = [
        "BolTest.java",
        "State.java",
        ":bol_scanner",
    ],
    data = ["bol-0.input"],
    deps = [
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
