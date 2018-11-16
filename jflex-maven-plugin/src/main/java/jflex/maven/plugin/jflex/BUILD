java_library(
    name = "jflex",
    srcs = glob(["*.java"]),
    visibility = ["//jflex-maven-plugin/src/test:__subpackages__"],
    deps = [
        "//jflex",
        "//jflex/src/main/java/jflex/generator",
        "//third_party/com/google/guava",
        "//third_party/org/apache/maven:maven_plugin_api",
        "@jsr305//jar",
    ],
    resources=[
        "//jflex-maven-plugin/java:meta_inf",
    ],
)
