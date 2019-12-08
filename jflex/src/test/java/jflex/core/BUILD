java_test(
    name = "CharClassesTest",
    srcs = ["CharClassesTest.java"],
    deps = [
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core",
        "//third_party/com/google/truth",
    ],
)

java_test(
    name = "IntCharSetProperties",
    srcs = [
        "IntCharSetGen.java",
        "IntCharSetProperties.java",
    ],
    deps = [
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core",
        "//third_party/com/google/truth",
        "//third_party/com/pholser/quickcheck",
    ],
)

java_test(
    name = "IntCharSetTest",
    srcs = ["IntCharSetTest.java"],
    deps = [
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core",
        "//third_party/com/google/truth",
    ],
)

java_test(
    name = "RegExpTests",
    srcs = ["RegExpTests.java"],
    deps = [
        "//jflex/src/main/java/jflex/chars",
        "//jflex/src/main/java/jflex/core",
        "//third_party/com/google/truth",
    ],
)

java_test(
    name = "SkeletonTest",
    srcs = ["SkeletonTest.java"],
    data = [
        "//jflex:resources",
    ],
    deps = [
        "//jflex:testing",
        "//jflex/src/main/java/jflex/core",
        "//third_party/com/google/truth",
    ],
)
