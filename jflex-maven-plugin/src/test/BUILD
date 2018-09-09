package(default_visibility = ["//jflex-maven-plugin/src/test:__subpackages__"])

load("//bazel:jar_rules.bzl", "simple_jar")

# Works around inability to add files in META-INF
#jar -tf bazel-bin/jflex-maven-plugin/src/test/java/de/jflex/plugin/maven/JFlexMojoTest.jar
#META-INF/
#META-INF/MANIFEST.MF
#test/
#test/META-INF/
#test/META-INF/maven/
#test/META-INF/maven/plugin.xml
simple_jar(
    name = "test_res",
    srcs = ["META-INF/maven/plugin.xml"],
)

filegroup(
    name = "test_data",
    srcs = glob(["projects/**"]),
)
