load("@jflex_rules//cup:cup.bzl", "cup")

package(default_visibility = ["//visibility:public"])

java_library(
    name = "core",
    srcs = glob(["**/*.java"]) + [
        "//jflex/src/main/java/jflex/core:gen_parser",
        "//jflex:gen_scanner",
    ],
    deps = [
        "//cup:cup_runtime",
        "//jflex/src/main/java/jflex/base",
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/io",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/logging",
        "//jflex/src/main/java/jflex/performance",
        "//jflex/src/main/java/jflex/scanner",
    ],
)

cup(
    name = "gen_parser",
    src = "//jflex/src/main/cup:LexParse.cup",
    cup_bin = "@jflex_rules//cup:cup_bin",
    interface = True,
    parser = "LexParse",
    symbols = "sym",
)
