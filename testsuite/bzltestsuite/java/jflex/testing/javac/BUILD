package(default_visibility = ["//testsuite:__subpackages__"])

java_library(
    name = "javac",
    srcs = glob(["*.java"]),
    javacopts = [
        # Only used for testing
        "-Xep:Java7ApiChecker:OFF",
    ],
    deps = [
        "//third_party/com/google/guava",
    ],
)
