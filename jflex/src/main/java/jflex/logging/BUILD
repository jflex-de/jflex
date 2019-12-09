java_library(
    name = "logging",
    srcs = ["Out.java"],
    visibility = ["//jflex:visibility"],
    deps = [
        ":stdout",
        "//jflex/src/main/java/jflex/base",
        "//jflex/src/main/java/jflex/env",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/performance",
    ],
)

java_library(
    name = "stdout",
    srcs = ["StdOutWriter.java"],
    deps = [
        "//jflex/src/main/java/jflex/env",
    ],
)
