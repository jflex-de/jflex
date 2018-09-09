java_test(
    name = "ClassInfoTest",
    srcs = ["ClassInfoTest.java"],
    deps = [
        "//jflex-maven-plugin/src/main",
    ]
)

java_test(
    name = "JFlexMojoTest",
    srcs = ["JFlexMojoTest.java"],
    deps = [
        "//jflex-maven-plugin/src/main",
        "//third_party/org/apache/maven/testing:maven_plugin_testing_harness",
    ],
    runtime_deps = [
        "//jflex-maven-plugin/src/test:res",
        "//third_party:org_apache_maven_maven_compat",
    ]
    
)
