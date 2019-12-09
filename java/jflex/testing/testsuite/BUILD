package(default_visibility = ["//jflex:visibility"])

java_library(
    name = "testsuite",
    testonly = True,
    srcs = glob(["*.java"]),
    exports = [
        "//java/jflex/testing/testsuite/annotations",
    ],
    deps = [
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/annotations",
        "//java/jflex/util/javac",
        "//jflex",
        "//jflex/src/main/java/jflex/generator",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
        "//third_party/junit",
    ],
)
