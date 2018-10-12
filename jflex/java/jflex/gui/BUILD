# It's not nice when high-level packages are nested inside their dep
# TODO(regisd) Move in a higher level different directory
java_library(
    name = "gui",
    srcs = glob(["*.java"]),
    deps = [
        "//jflex/java/jflex",
        "//jflex/java/jflex/exception",
    ],
    visibility = ["//jflex:__pkg__"],
)
