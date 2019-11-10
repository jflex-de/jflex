load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_unicode_data_scanner",
    srcs = glob(["*.flex"]),
    outputs = [
        "UnicodeDataScanner.java",
    ],
    visibility = ["//java/jflex/ucd_generator:__subpackages__"],
)

java_library(
    name = "UnicodeData",
    srcs = ["UnicodeData.java"],
    deps = [
        "//third_party/com/google/auto_value",
        "//java/jflex/ucd_generator/ucd",
    ],
)