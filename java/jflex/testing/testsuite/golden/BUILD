java_library(
    name = "golden",
    testonly = True,
    srcs = ["AbstractGoldenTest.java"],
    visibility = ["//visibility:public"],
    exports = [":model"],
    deps = [
        ":model",
        "//java/jflex/testing/diff",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
        "//third_party/junit",
    ],
)

java_library(
    name = "model",
    srcs = ["GoldenInOutFilePair.java"],
    visibility = ["//visibility:public"],
    deps = [],
)
