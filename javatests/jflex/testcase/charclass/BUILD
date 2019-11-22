load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_charclass_scanner",
    srcs = ["charclass.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["CharclassScanner.java"],
)

java_test(
    name = "CharclassTest",
    srcs = [
        "CharclassTest.java",
        "State.java",
        ":gen_charclass_scanner",
    ],
    deps = [
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
