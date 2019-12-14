load("@jflex_rules//jflex:jflex.bzl", "jflex")

java_library(
    name = "ucs_scanners",
    srcs = [
        "AbstractBinaryPropertiesFileScanner.java",
        "AbstractPropertyAliasesScanner.java",
        "AbstractPropertyValueAliasesScanner.java",
        "AbstractUnicodeDataScanner.java",
        ":gen_ucd_scanners",
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
    name = "gen_ucd_scanners",
    srcs = glob(["*.flex"]),
    outputs = [
        "BinaryPropertiesFileScanner.java",
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
