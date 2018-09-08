load("//bazel:cup_rules.bzl", "cup")

java_library(
    name = "jflex",
    srcs = glob(["src/main/java/**"]),
    deps = [
        ":parser",
        "//jflex/src/main/java/jflex/base",
    ],
)

java_library(
    name = "parser",
    srcs = [":gen_parser"],
    deps = [
        "//cup/cup_runtime",
        "//jflex/src/main/java/jflex/unicode",
    ],
)

cup(
    name = "gen_parser",
    srcs = ["src/main/cup/LexParse.cup"],
    parser = "LexParse",
    symbols = "sym",
)

maven_jar(
    name = "jflex-1.6.1",
    artifact = "de.jflex:jflex:1.6.1",
    sha1 = "eb4d51e1a8ea7ee96068905ddeceb9b28737c7eb",
)
