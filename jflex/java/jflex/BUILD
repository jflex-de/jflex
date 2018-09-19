load("//bazel:cup_rules.bzl", "cup")

java_library(
    name = "jflex",
    srcs = glob(["**/*.java"]) + [
        "//jflex/lex:gen_parser",
        "//jflex/lex:gen_scanner",
    ],
    resources = [
                        "Messages.properties",
                        "skeleton.default",
                    ],
    deps = [
        "//third_party/java_cup",
        "//jflex/java/jflex/base",
        "//jflex/java/jflex/exception",
        "//jflex/java/jflex/unicode",
        "//third_party/org/apache/ant",
    ],
    visibility = ["//visibility:public"],
)
