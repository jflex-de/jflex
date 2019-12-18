# https://versioncontrolseidl.in.tum.de/parsergenerators/cup2/tree/master

licenses(["notice"])  # zlib/libpng License

exports_files(["license.txt"])

java_library(
    name = "cup2",
    srcs = glob(["**/*.java"]),
    javacopts = ["-XepDisableAllChecks"],
    visibility = ["//visibility:public"],
    deps = [
        "//third_party/org/apache/ant",
    ],
)
