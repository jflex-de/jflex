SRCS_MAIN = ["Main.java"]

java_library(
    name = "ucd_generator",
    srcs = glob(
        ["*.java"],
        exclude = SRCS_MAIN,
    ),
)

java_binary(
    name = "Main",
    srcs = SRCS_MAIN,
    deps = [":ucd_generator"],
)

genrule(
    name = "gen_unicode_properties",
    srcs = ["//third_party/unicode:ucd_1"],
    outs = ["logs.txt"],
    cmd = "$(location :Main) $< > $@",
    tools = [":Main"],
)
