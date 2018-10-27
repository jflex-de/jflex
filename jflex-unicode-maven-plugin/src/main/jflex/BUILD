load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_ucd_scanners",
    srcs = glob(["*.flex"]),
    outputs = [
        "ArchaicBlocksScanner.java",
        "ArchaicLineBreakScanner.java",
        "ArchaicPropListScanner.java",
        "BinaryPropertiesFileScanner.java",
        "EnumeratedPropertyFileScanner.java",
        "PropertyAliasesScanner.java",
        "PropertyValueAliasesScanner.java",
        "ScriptExtensionsScanner.java",
        "UnicodeDataScanner.java",
    ],
    visibility = ["//jflex-unicode-maven-plugin/src/main:__subpackages__"],
)
