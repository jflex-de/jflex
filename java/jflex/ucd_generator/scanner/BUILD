load("@jflex_rules//jflex:jflex.bzl", "jflex")

java_library(
    name = "unicode_data_scanner",
    srcs = [
        "AbstractPropertyAliasesScanner.java",
        "AbstractPropertyValueAliasesScanner.java",
        "AbstractUnicodeDataScanner.java",
        ":gen_unicode_data_scanner",
    ],
    visibility = [
        "//java/jflex/ucd_generator:__subpackages__",
        "//javatests/jflex:__subpackages__",
    ],
    deps = [
        ":name_normalizer",
        ":unicode_data",
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/ucd_generator/util:surrogate_utils",
        "//third_party/com/google/guava",
    ],
)

jflex(
    name = "gen_unicode_data_scanner",
    srcs = glob(["*.flex"]),
    outputs = [
        "PropertyAliasesScanner.java",
        "PropertyValueAliasesScanner.java",
        "UnicodeDataScanner.java",
    ],
    visibility = ["//java/jflex/ucd_generator:__subpackages__"],
)

java_library(
    name = "unicode_data",
    srcs = [
        "PropertyValues.java",
        "UnicodeData.java",
    ],
    visibility = [
        "//java/jflex/ucd_generator:__subpackages__",
        "//javatests/jflex:__subpackages__",
    ],
    deps = [
        ":name_normalizer",
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/ucd_generator/util:hexa_utils",
        "//java/jflex/ucd_generator/util:surrogate_utils",
        "//third_party/com/google/auto_value",
        "//third_party/com/google/guava",
    ],
)

java_library(
    name = "name_normalizer",
    srcs = ["PropertyNameNormalizer.java"],
    deps = [
    ],
)
