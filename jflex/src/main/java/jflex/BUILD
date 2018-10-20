package(default_visibility = ["//visibility:public"])

java_library(
    name = "jflex",
    srcs = glob(["**/*.java"]) + [
        "//jflex:gen_parser",
        "//jflex:gen_scanner",
    ],
    resources = ["//jflex:resources"],
    deps = [
        "//cup:cup_runtime",
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/io",
        "//jflex/src/main/java/jflex/performance",
        "//third_party/org/apache/ant",
    ],
)
