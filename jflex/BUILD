load("//bazel:cup_rules.bzl", "cup")

cup(
    name = "gen_parser",
    srcs = ["src/main/cup/LexParse.cup"],
    parser = "LexParse",
    symbols = "sym",
)

java_library(
    name = "parser",
    srcs = [":gen_parser"],
    deps = [
        "//cup/cup_runtime",
        "//jflex/src/main/java/jflex/unicode",
    ],
)

java_library(
    name = "jflex",
    srcs = glob(["src/main/java/**"]),
    deps = [
        ":parser",
        "//jflex/src/main/java/jflex/base",
    ],
)
