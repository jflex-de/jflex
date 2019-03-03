SRCS_MAIN = ["Main.java"]

java_library(
    name = "unicode_properties",
    srcs = glob(["*.java"]),
    resources = [
        "UnicodeProperties.java.vm",
    ],
    visibility = [
        "//java/jflex/ucd_generator:__pkg__",
        "//javatests/jflex/ucd_generator:__subpackages__",
    ],
    deps = [
        "//java/jflex/testing/javac",
        "//java/jflex/ucd_generator/emitter/common",
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/velocity",
        "//third_party/com/google/guava",
        "//third_party/org/apache/velocity",
    ],
)
