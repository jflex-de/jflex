package(default_visibility = ["//jflex:visibility"])

SRCS_DEPRECATED_DFA = [
    "DeprecatedDfa.java",
    "StatePairList.java",
]

java_library(
    name = "dfa",
    srcs = glob(["*.java"]),
    deps = [
        "//jflex/src/main/java/jflex/core",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/logging",
        "//jflex/src/main/java/jflex/option",
        "//jflex/src/main/java/jflex/state",
    ],
)

java_library(
    name = "deprecated_dfa",
    testonly = True,
    srcs = SRCS_DEPRECATED_DFA,
    deps = [
        "//jflex/src/main/java/jflex/dfa",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/l10n",
        "//jflex/src/main/java/jflex/logging",
        "//jflex/src/main/java/jflex/option",
    ],
)
