java_library(
    name = "maven",
    srcs = glob(["*.java"]),
    deps = [
        "//jflex",
        "//third_party/commons_io",
        "//third_party/org/apache/maven:maven_core",
        "//third_party/org/apache/maven:maven_plugin_api",
        "//third_party/org/apache/maven/plugin_tools:maven_plugin_annotations",
    ],
    visibility = ["//jflex-maven-plugin/src/main:__pkg__"],
)
