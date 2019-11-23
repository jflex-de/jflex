licenses(["notice"])  # BSD-like

java_library(
    name = "cup_runtime",
    srcs = glob(["src/main/java/**/*.java"]),
    javacopts = [
        # Not our code, there is nothing we can do
        "-XepDisableAllChecks",
    ],
    visibility = ["//visibility:public"],
)
