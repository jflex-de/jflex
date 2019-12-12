java_test(
    name = "IntCharSetQuickcheck",
    srcs = [
        "IntCharSetGen.java",
        "IntCharSetQuickcheck.java",
    ],
    deps = [
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core",
        "//jflex/src/main/java/jflex/core/unicode",
        "//jflex/src/test/java/jflex/chars",
        "//third_party/com/google/truth",
        "//third_party/com/pholser/quickcheck",
    ],
    timeout = "short",
)

java_test(
    name = "IntCharSetTest",
    srcs = ["IntCharSetTest.java"],
    deps = [
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core",
        "//jflex/src/main/java/jflex/core/unicode",
        "//third_party/com/google/truth",
    ],
    timeout = "short",
)
