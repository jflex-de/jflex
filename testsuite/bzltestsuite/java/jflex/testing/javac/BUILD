package(default_visibility = ["//testsuite:__subpackages__"])

java_library(
    name = "javac",
    srcs = glob(["*.java"]),
    deps = [
        "//third_party/com/google/guava",
    ],
    javacopts = [
        # Only used for testing
        "-Xep:Java7ApiChecker:OFF",
        ],
)
