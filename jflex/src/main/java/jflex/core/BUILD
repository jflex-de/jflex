package(default_visibility = ["//visibility:public"])

java_library(
    name = "core",
    srcs = glob(["**/*.java"]) + [
        "//jflex:gen_parser",
        "//jflex:gen_scanner",
    ],
    deps = [
        "//cup:cup_runtime",
        "//jflex/src/main/java/jflex/base",
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/io",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/performance",
        "//jflex/src/main/java/jflex/scanner",
    ],
)

# Used as a golden file by ucd_generator
exports_files(["unicode/UnicodeProperties.java"])
