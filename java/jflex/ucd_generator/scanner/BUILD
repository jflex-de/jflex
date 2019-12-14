load("@jflex_rules//jflex:jflex.bzl", "jflex")

java_library(
    name = "ucd_scanners",
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
        "//java/jflex/ucd_generator/scanner/model",
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/ucd_generator/util:name_normalizer",
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
