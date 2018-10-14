package(default_visibility = [
    "//testsuite:__subpackages__",
    "//jflex/examples:__subpackages__",
    "//jflex/common-testing:__subpackages__",
])

java_library(
    name = "testing",
    srcs = glob(["*.java"]),
    testonly = True,
    deps = [
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
