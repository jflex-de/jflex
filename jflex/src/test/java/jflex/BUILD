java_test(
    name = "CharClassesTest",
    srcs = ["CharClassesTest.java"],
    deps = [
        "//jflex",
        "//jflex/src/main/java/jflex/unicode",
    ],
)

java_test(
    name = "EmitterTest",
    srcs = ["EmitterTest.java"],
    deps = [
        "//jflex",
    ],
)

java_test(
    name = "PackEmitterTest",
    srcs = ["PackEmitterTest.java"],
    deps = [
        "//jflex",
    ],
)

java_test(
    name = "RegExpTests",
    srcs = ["RegExpTests.java"],
    deps = [
        "//jflex",
        "//jflex/src/main/java/jflex/unicode",
    ],
)

java_test(
    name = "SkeletonTest",
    srcs = ["SkeletonTest.java"],
    resources = [

        "//jflex/src/test:test_resources",
    ],
    deps = [
        "//jflex",
    ],
    data =[
        "//jflex/src/main/jflex:skeleton.nested",
    ]

)
