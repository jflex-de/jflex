package(default_visibility = ["//visibility:public"])

java_library(
    name = "javac",
    srcs = [
        "CompilerException.java",
        "JavacUtil.java",
    ],
    javacopts = [
        # Only used for testing
        "-Xep:Java7ApiChecker:OFF",
    ],
    deps = [
        "//third_party/com/google/guava",
    ],
)

java_library(
    name = "package_util",
    srcs = ["JavaPackageUtil.java"],
    deps = [
    ],
)
