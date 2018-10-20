package(default_visibility = ["//visibility:public"])

LAUNCHER_SRCS = ["Main.java"]

java_library(
    name = "jflex",
    srcs = glob(["**/*.java"], exclude = LAUNCHER_SRCS) + [
        "//jflex:gen_parser",
        "//jflex:gen_scanner",
    ],
    resources = ["//jflex:resources"],
    deps = [
        "//cup:cup_runtime",
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/io",
        "//jflex/src/main/java/jflex/performance",
        "//third_party/com/google/auto_value",
    ],
)

# cli, gui, and ant task
java_library(
    name = "jflex_ui",
    srcs = LAUNCHER_SRCS,
    deps = [
        ":jflex",
        "//jflex/src/main/java/jflex/anttask",
        "//jflex/src/main/java/jflex/gui",
    ],
)
