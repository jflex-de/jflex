SRCS_MAIN = ["Main.java"]

java_library(
    name = "ucd_generator",
    srcs = glob(
        ["*.java"],
        exclude = SRCS_MAIN,
    ),
    deps = [
        "//third_party/com/google/guava",
    ],
)

java_binary(
    name = "Main",
    srcs = SRCS_MAIN,
    deps = [
        ":ucd_generator",
        "//third_party/com/google/guava",
    ],
)

genrule(
    name = "gen_unicode_properties",
    srcs = ["//third_party/unicode:ucd_1"],
    outs = ["logs.txt"],
    cmd = "$(location :Main) 1=$(location //third_party/unicode:ucd_1) > $@",
    tools = [":Main"],
)
