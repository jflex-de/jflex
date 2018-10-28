SRCS_MAIN = ["Main.java"]

java_library(
    name = "ucd_generator",
    srcs = glob(
        ["*.java"],
        exclude = SRCS_MAIN,
    ),
    deps = [
        "//java/velocity",
        "//third_party/com/google/guava",
        "//third_party/org/apache/velocity",
    ],
    resources = [
        "UnicodeProperties.java.vm",
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
    srcs = ["//third_party/unicode:ucd_9"],
    outs = ["logs.txt"],
    cmd = "$(location :Main) --version=9 $(locations //third_party/unicode:ucd_9) > $@",
    tools = [":Main"],
)
