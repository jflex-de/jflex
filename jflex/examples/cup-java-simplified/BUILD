package(default_visibility = ["//visibility:public"])

load("@jflex_rules//jflex:jflex.bzl", "jflex")
load("//cup:cup.bzl", "cup")

# The best practice is to define the rules in their respective directory in
# - src/main/java/org/example/foo/BUILD
# - src/main/jflex/BUILD
# - src/test/java/org/example/foo/BUILD
# - etc.
# However, this example is simple enough and we can define all rules here.

java_binary(
    name = "java_simple_bin",
    main_class = "Yylex",
    runtime_deps = [":java_simple"],
)

java_library(
    name = "java_simple",
    # glob is not a best practice, but it's good enough for this example
    srcs = glob(["src/main/java/**/*.java"]) + [":gen_lexer",],
    deps = ["//cup:cup_runtime"],
)

jflex(
    name = "gen_lexer",
    srcs = ["src/main/jflex/java_simplified.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Lexer.java"],
)

cup(
    name = "gen_parser",
    src = "src/main/cup/java_simplified.cup",
    symbols = "sym",
)