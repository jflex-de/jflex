load("//bazel:cup_rules.bzl", "cup")

java_library(
    name = "main",
    srcs = glob(["java/**/*.java"]) + [
        ":gen_parser",
        "//jflex/src/main/jflex:gen_scanner",
    ],
    resources = [
        "resources/jflex/Messages.properties",
        "resources/jflex/skeleton.default",
    ],
    deps = [
        "//cup/cup_runtime",
        "//jflex/src/main/java/jflex/base",
        "//jflex/src/main/java/jflex/exception",
        "//jflex/src/main/java/jflex/unicode",
        "//third_party/org/apache/ant",
    ],
    visibility = ["//visibility:public"],
)

cup(
    name = "gen_parser",
    srcs = ["cup/LexParse.cup"],
    parser = "LexParse",
    symbols = "sym",
    interface = True,
)
#wtf regis