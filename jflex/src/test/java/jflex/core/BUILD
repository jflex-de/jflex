java_test(
    name = "IntCharSetQuickcheck",
    srcs = [
        "IntCharSetGen.java",
        "IntCharSetQuickcheck.java",
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
    name = "RegExpTest",
    srcs = ["RegExpTest.java"],
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
