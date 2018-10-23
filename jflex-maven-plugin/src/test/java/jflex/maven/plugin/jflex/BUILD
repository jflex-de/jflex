java_test(
    name = "ClassInfoTest",
    srcs = ["ClassInfoTest.java"],
    deps = [
        "//jflex-maven-plugin/src/main/java/jflex/maven/plugin/jflex",
        "//third_party/com/google/truth",
    ],
)

java_test(
    name = "JFlexMojoTest",
    srcs = ["JFlexMojoTest.java"],
    deps = [
        "//jflex-maven-plugin/src/main/java/jflex/maven/plugin/jflex",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
        # maven_core (which injects Sisu PlexusContainer) must be called *before* testing_harness
        # Otherwise injection will fail with:
        # NoSuchMethodError: org.codehaus.plexus.DefaultPlexusContainer.<init>
        "//third_party/jvm/org/apache/maven:maven_core",
        "//third_party/jvm/org/apache/maven/plugin_testing:maven_plugin_testing_harness",
    ],
)

java_test(
    name = "LexSimpleAnalyzerTest",
    srcs = ["LexSimpleAnalyzerTest.java"],
    deps = [
        "//jflex-maven-plugin/src/main/java/jflex/maven/plugin/jflex",
        "//third_party/com/google/truth",
    ],
)
