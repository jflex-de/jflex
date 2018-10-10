load("//third_party/de/jflex:build_rules.bzl", "jflex")

# The best practice is to define the rules in their respective directory in
# - src/main/java/org/example/foo/BUILD
# - src/main/jflex/BUILD
# - src/test/java/org/example/foo/BUILD
# - etc.
# However, this example is simple enough and we can define all rules here.

java_binary(
    name = "simple_bin",
    main_class = "Yylex",
    runtime_deps = [":simple"],
)

java_library(
    name = "simple",
    # glob is not a best practice, but it's good enough for this example
    srcs = glob(["src/main/java/**/*.java"]) + [":gen_lexer"],
)

jflex(
    name = "gen_lexer",
    srcs = ["src/main/jflex/simple.flex"],
    outputs = ["Yylex.java"],
)

# Test disabled because Bazel uses JFlex 1.6.1
# And the dump has changed (i.e. the golden file is not coorect)
# TODO(#390) Use JFlex 1.7.0 and re-enabled the test when java_cup has been published on mvn repo
#java_test(
#    name = "YylexTest",
#    srcs = ["src/test/java/YylexTest.java"],
#    data = glob(["src/test/data/**"]),
#    deps = [
#        ":simple",
#        "//third_party/com/google/truth",
#        "//third_party/com/google/guava"
#        ],
#)
