java_library(
    name = "unicode",
    srcs = glob(["data/*.java"]) + [
        "CharClasses.java",
        "IntCharSet.java",
        "UnicodeProperties.java",
        "CharClassInterval.java",
        "ILexScan.java",
    ],
    visibility = ["//jflex:__subpackages__"],
    deps = [
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/env",
        "//jflex/src/main/java/jflex/logging",
    ],
)
