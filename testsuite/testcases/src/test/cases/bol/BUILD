load("//bazel:jflex_rules.bzl", "jflex")

jflex(
    name = "gen_java_" + "bol",
    srcs = ["bol.flex"],
    outputs = ["Bol.java"],
)

java_binary(
    name = "bol",
    srcs = [
      ":gen_java_" + "bol",
#      "Test.java",
    ],
    main_class = "Bol",
)

genrule(
    name = "parse_input",
    cmd = "$(location :bol) $(SRCS) > $(@)",
    srcs = ["bol-0.input"],
    outs = ["bol-0.txt"],
    tools = [":bol"],
)

