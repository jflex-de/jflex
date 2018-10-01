load("//third_party/de/jflex:build_rules.bzl", "jflex")

java_library(
    name = "main",
    srcs = [
        "java/Utility.java",
        "java/Yytokn.java",
        ":gen_lexer",
    ],
)

jflex(
    name = "gen_lexer",
    srcs = ["jflex/simple.flex"],
    outputs = ["Yylex.java"],
)
