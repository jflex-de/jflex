package(default_visibility = ["//jflex:visibility"])

java_library(
    name = "golden",
    testonly = True,
    srcs = ["AbstractGoldenTest.java"],
    exports = [":model"],
    deps = [
        ":model",
        "//java/jflex/testing/diff",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
        "//third_party/junit",
    ],
)

java_library(
    name = "model",
    srcs = ["GoldenInOutFilePair.java"],
    deps = [],
)
