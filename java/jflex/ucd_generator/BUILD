package(default_visibility=["//javatests/jflex/ucd_generator:__subpackages__"])

SRCS_MAIN = ["Main.java"]

java_library(
    name = "ucd_generator",
    srcs = glob(
        ["*.java"],
        exclude = SRCS_MAIN,
    ),
    resources = [
        "UnicodeProperties.java.vm",
    ],
    deps = [
        "//java/jflex/testing/javac",
        "//java/jflex/velocity:velocity",
        "//third_party/com/google/guava",
        "//third_party/org/apache/velocity",
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

#TODO(regisd) Move in //jflex/core/jnicode when migration to bazel is completed
genrule(
    name = "gen_unicode_properties",
    srcs = [
        "//third_party/unicode:ucd_1",
        "//third_party/unicode:ucd_2",
        "//third_party/unicode:ucd_3",
        "//third_party/unicode:ucd_4",
        "//third_party/unicode:ucd_5",
        "//third_party/unicode:ucd_6",
        "//third_party/unicode:ucd_7",
        "//third_party/unicode:ucd_8",
        "//third_party/unicode:ucd_9",
        ],
    outs = ["UnicodeProperties.java"],
    cmd = "$(location :Main)" +
        " --version=1.1.5 $(locations //third_party/unicode:ucd_1)" +
        " --version=2.1.9 $(locations //third_party/unicode:ucd_2)" +
        " --version=3.2.0 $(locations //third_party/unicode:ucd_3)" +
        " --version=4.1.0 $(locations //third_party/unicode:ucd_4)" +
        " --version=5.2.0 $(locations //third_party/unicode:ucd_5)" +
        " --version=6.3.0 $(locations //third_party/unicode:ucd_6)" +
        " --version=7.0.0 $(locations //third_party/unicode:ucd_7)" +
        " --version=8.0.0 $(locations //third_party/unicode:ucd_8)" +
        " --version=9.0.0 $(locations //third_party/unicode:ucd_9)" +
        " > $@",
    tools = [":Main"],
)
