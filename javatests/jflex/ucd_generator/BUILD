java_test(
    name = "EmitterTest",
    srcs = ["EmitterTest.java"],
    data = [
        "UnicodeProperties.java.golden",
    ],
    deps = [
        "//java/jflex/testing/diff",
        "//java/jflex/testing/javac",
        "//java/jflex/ucd_generator",
        "//third_party/com/google/truth",
    ],
)

java_test(
    name = "UcdVersionsTest",
    srcs = ["UcdVersionsTest.java"],
    deps = [
        "//java/jflex/ucd_generator",
        "//third_party/com/google/truth",
    ],
)
