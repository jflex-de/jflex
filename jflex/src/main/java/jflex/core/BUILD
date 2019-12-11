java_library(
    name = "core",
    srcs = glob(["*.java"]) + [
        "//jflex:gen_parser",
        "//jflex:gen_scanner",
    ],
    visibility = ["//jflex:visibility"],
    deps = [
        "//cup:cup_runtime",
        "//jflex/src/main/java/jflex/base",
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core/unicode",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/io",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/logging",
        "//jflex/src/main/java/jflex/option",
        "//jflex/src/main/java/jflex/performance",
        "//jflex/src/main/java/jflex/scanner",
        "//jflex/src/main/java/jflex/state",
    ],
)
