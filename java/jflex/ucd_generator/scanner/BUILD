load("@jflex_rules//jflex:jflex.bzl", "jflex")

java_library(
    name = "unicode_data_scanner",
    srcs = [
        ":gen_unicode_data_scanner",
    ],
    deps = [
        ":unicode_data",
        "//java/jflex/ucd_generator/ucd",
    ],
    visibility=[
         "//javatests/jflex:__subpackages__",
    ],
)

jflex(
    name = "gen_unicode_data_scanner",
    srcs = glob(["*.flex"]),
    outputs = [
        "UnicodeDataScanner.java",
    ],
    visibility = ["//java/jflex/ucd_generator:__subpackages__"],
)

java_library(
    name = "unicode_data",
    srcs = ["UnicodeData.java"],
    deps = [
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/ucd_generator/util:hexa_utils",
        "//third_party/com/google/auto_value",
        "//third_party/com/google/guava",
    ],
    visibility = [
        "//java/jflex/ucd_generator:__subpackages__",
        "//javatests/jflex:__subpackages__",
    ],
)
