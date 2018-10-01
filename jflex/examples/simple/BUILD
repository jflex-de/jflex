load("//third_party/de/jflex:build_rules.bzl", "jflex")

java_binary(
    name="simple_bin",
    main_class = "Yylex",
    runtime_deps = [":simple"],
)

java_library(
    name = "simple",
    # glob is not a best practice, but it's good enough for this example
    srcs = glob(["src/main/java/**/*.java"])  + [":gen_lexer"],
)

jflex(
    name = "gen_lexer",
    srcs = ["src/main/jflex/simple.flex"],
    outputs = ["Yylex.java"],
)
