java_test(
    name = "UnicodePropertiesEmitterTest",
    srcs = ["UnicodePropertiesEmitterTest.java"],
    data = [
        "UnicodeProperties.java.golden",
    ],
    deps = [
        "//java/jflex/testing/diff",
        "//java/jflex/ucd_generator",
        "//java/jflex/ucd_generator/emitter/unicode_properties",
        "//java/jflex/ucd_generator/ucd",
        "//java/jflex/util/javac",
        "//third_party/com/google/truth",
    ],
)
