SRCS_MAIN = ["Main.java"]

java_library(
    name = "unicode_version",
    srcs = glob(["*.java"]),
    resources = [
        "Unicode_x_y.java.vm",
    ],
    visibility = [
        "//java/jflex/ucd_generator:__pkg__",
        "//javatests/jflex/ucd_generator:__subpackages__",
    ],
    deps = [
        "//java/jflex/ucd_generator/emitter/common",
        "//java/jflex/ucd_generator/scanner:unicode_data",
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/util/javac:package_util",
        "//java/jflex/velocity",
        "//third_party/com/google/guava",
        "//third_party/org/apache/velocity",
    ],
)
