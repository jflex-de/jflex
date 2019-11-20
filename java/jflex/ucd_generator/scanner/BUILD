load("@jflex_rules//jflex:jflex.bzl", "jflex")

java_library(
    name = "unicode_scanners",
    srcs = [
        ":gen_property_value_aliases_scanner",
        ":gen_unicode_data_scanner",
    ],
    visibility = [
        "//java/jflex/ucd_generator:__subpackages__",
        "//javatests/jflex:__subpackages__",
    ],
    deps = [
        ":unicode_data",
        ":utils",
        "//java/jflex/ucd_generator/ucd",
    ],
)

jflex(
    name = "gen_property_value_aliases_scanner",
    srcs = ["PropertyValueAliasesScanner.flex"],
    outputs = ["PropertyValueAliasesScanner.java"],
    visibility = ["//java/jflex/ucd_generator:__subpackages__"],
)

jflex(
    name = "gen_unicode_data_scanner",
    srcs = ["UnicodeDataScanner.flex"],
    outputs = ["UnicodeDataScanner.java"],
    visibility = ["//java/jflex/ucd_generator:__subpackages__"],
)

java_library(
    name = "unicode_data",
    srcs = ["UnicodeData.java"],
    visibility = [
        "//java/jflex/ucd_generator:__subpackages__",
        "//javatests/jflex:__subpackages__",
    ],
    deps = [
        ":utils",
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/ucd_generator/util:hexa_utils",
        "//third_party/com/google/auto_value",
        "//third_party/com/google/guava",
    ],
)

java_library(
    name = "utils",
    srcs = [
        "PropertyNameNormalizer.java",
        "SurrogateUtils.java",
    ],
    deps = [
        "//java/jflex/ucd_generator/ucd",
        "//third_party/com/google/guava",
    ],
)
