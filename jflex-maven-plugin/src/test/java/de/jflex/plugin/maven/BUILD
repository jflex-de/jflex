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
        "//third_party/org/codehaus/plexus:plexus_classworlds", 
        "//third_party/org/codehaus/plexus:plexus_container_default", 
        "//third_party/org/codehaus/plexus:plexus_utils", 
        "//third_party/org/apache/maven:maven_artifact",
        "//third_party/org/apache/maven:maven_core",
        "//third_party/org/eclipse/sisu",
    ],
)
