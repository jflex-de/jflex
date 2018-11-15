package(default_visibility = ["//visibility:public"])

java_library(
    name = "testsuite",
    testonly = True,
    srcs = glob(["*.java"]),
    exports = [
        "//java/jflex/testing/testsuite/annotations",
    ],
    deps = [
        "//java/jflex/testing/assertion",
        "//java/jflex/testing/javac",
        "//java/jflex/testing/testsuite/annotations",
        "//jflex",
        "//jflex/src/main/java/jflex/generator",
        "//third_party/com/google/guava",
        "//third_party/junit",
    ],
)
