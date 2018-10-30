package(default_visibility = ["//javatests/jflex/ucd_generator:__subpackages__"])

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
        "//java/jflex/velocity",
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
        "//third_party/unicode:ucd_1_1_5",
        "//third_party/unicode:ucd_2_0_14",
        "//third_party/unicode:ucd_2_1_9",
        "//third_party/unicode:ucd_3_0_1",
        "//third_party/unicode:ucd_3_1_1",
        "//third_party/unicode:ucd_3_2_0",
        "//third_party/unicode:ucd_4_0_1",
        "//third_party/unicode:ucd_4_1_0",
        "//third_party/unicode:ucd_5_0_0",
        "//third_party/unicode:ucd_5_1_0",
        "//third_party/unicode:ucd_5_2_0",
        "//third_party/unicode:ucd_6_0_0",
        "//third_party/unicode:ucd_6_1_0",
        "//third_party/unicode:ucd_6_2_0",
        "//third_party/unicode:ucd_6_3_0",
        "//third_party/unicode:ucd_7",
        "//third_party/unicode:ucd_8",
        "//third_party/unicode:ucd_9",
    ],
    outs = ["UnicodeProperties.java"],
    cmd = "$(location :Main)" +
          " --version=1.1.5 $(locations //third_party/unicode:ucd_1_1_5)" +
          " --version=2.0.14 $(locations //third_party/unicode:ucd_2_0_14)" +
          " --version=2.1.9 $(locations //third_party/unicode:ucd_2_1_9)" +
          " --version=3.0.1 $(locations //third_party/unicode:ucd_3_0_1)" +
          " --version=3.1.1 $(locations //third_party/unicode:ucd_3_1_1)" +
          " --version=3.2.0 $(locations //third_party/unicode:ucd_3_2_0)" +
          " --version=4.0.1 $(locations //third_party/unicode:ucd_4_0_1)" +
          " --version=4.1.0 $(locations //third_party/unicode:ucd_4_1_0)" +
          " --version=5.0.0 $(locations //third_party/unicode:ucd_5_0_0)" +
          " --version=5.1.0 $(locations //third_party/unicode:ucd_5_1_0)" +
          " --version=5.2.0 $(locations //third_party/unicode:ucd_5_2_0)" +
          " --version=6.0.0 $(locations //third_party/unicode:ucd_6_0_0)" +
          " --version=6.1.0 $(locations //third_party/unicode:ucd_6_1_0)" +
          " --version=6.2.0 $(locations //third_party/unicode:ucd_6_2_0)" +
          " --version=6.3.0 $(locations //third_party/unicode:ucd_6_3_0)" +
          " --version=7.0.0 $(locations //third_party/unicode:ucd_7)" +
          " --version=8.0.0 $(locations //third_party/unicode:ucd_8)" +
          " --version=9.0.0 $(locations //third_party/unicode:ucd_9)" +
          " > $@",
    tools = [":Main"],
)
