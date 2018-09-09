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
        "//third_party/aopalliance",
        "//third_party/com/google/guava",
        "//third_party/com/google/inject:guice",
        "//third_party/org/codehaus/plexus:plexus_component_annotations", 
        "//third_party/org/codehaus/plexus:plexus_classworlds", 
        "//third_party/org/codehaus/plexus:plexus_utils", 
        "//third_party/org/apache/maven:maven_artifact",
        "//third_party/org/apache/maven:maven_compat",
        "//third_party/org/apache/maven:maven_core",
        "//third_party/org/apache/maven:maven_model",
        "//third_party/org/apache/maven:maven_model_builder",
        "//third_party/org/apache/maven:maven_repository_metadata",
        "//third_party/org/apache/maven:maven_settings",
        "//third_party/org/apache/maven:maven_settings_builder",
        "//third_party/org/apache/maven/resolver:maven_resolver_impl",
        "//third_party/org/apache/maven/wagon:wagon_provider_api",
        "//third_party/org/eclipse/sisu",
        "//third_party/org/eclipse/sisu:sisu_inject",
        "//third_party/org/eclipse/aether:aether_api",
        "//third_party/org/eclipse/aether:aether_impl",
    ],
)
