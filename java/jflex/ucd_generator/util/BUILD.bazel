package(default_visibility = ["//java/jflex/ucd_generator:__subpackages__"])

java_library(
    name = "hexa_utils",
    srcs = ["HexaUtils.java"],
    visibility = ["//visibility:public"],
    deps = [
        "//third_party/com/google/guava",
    ],
)

java_library(
    name = "name_normalizer",
    srcs = ["PropertyNameNormalizer.java"],
    visibility = [
        "//java/jflex/ucd_generator:__subpackages__",
        "//javatests/jflex:__subpackages__",
    ],
    deps = [
    ],
)

java_library(
    name = "surrogate_utils",
    srcs = [
        "SurrogateUtils.java",
    ],
    deps = [
        "//java/jflex/ucd_generator/ucd",
        "//third_party/com/google/guava",
    ],
)
