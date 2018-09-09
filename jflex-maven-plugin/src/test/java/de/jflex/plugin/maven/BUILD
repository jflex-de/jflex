load("//bazel:maven_rules.bzl", "maven_plugin_test")

java_test(
    name = "ClassInfoTest",
    srcs = ["ClassInfoTest.java"],
    deps = [
        "//jflex-maven-plugin/src/main",
    ]
)

maven_plugin_test(
    name = "JFlexMojoTest",
    srcs = ["JFlexMojoTest.java"],
    deps = [
        "//jflex-maven-plugin/src/main",
    ],
    runtime_deps = [
        "//jflex-maven-plugin/src/test:test_res",
    ],
    data = [
        "//jflex-maven-plugin/src/test:test_data",
    ],  
)
