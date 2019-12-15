java_test(
    name = "UcdGeneratorIntegrationTest",
    srcs = ["UcdGeneratorIntegrationTest.java"],
    data = [
        "//third_party/unicode:ucd_10",
    ],
    deps = [
        ":const",
        "//java/jflex/ucd_generator",
        "//java/jflex/ucd_generator/ucd",
        "//third_party/com/google/truth",
    ],
)

java_test(
    name = "UcdScannerTest",
    srcs = ["UcdScannerTest.java"],
    data = [
        "//third_party/unicode:ucd_10",
    ],
    deps = [
        ":const",
        "//java/jflex/ucd_generator",
        "//java/jflex/ucd_generator/ucd",
        "//third_party/com/google/truth",
    ],
)

java_library(
    name = "const",
    srcs = ["TestedVersions.java"],
    deps = [
        "//java/jflex/ucd_generator/ucd",
    ],
)
