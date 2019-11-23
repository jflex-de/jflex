load("@jflex_rules//jflex:jflex.bzl", "jflex")

java_binary(
    name = "migration",
    srcs = [],
    main_class = "jflex.migration.Migrator",
    runtime_deps = [":migrator"],
)

java_library(
    name = "migrator",
    srcs = [
        "MigrationException.java",
        "Migrator.java",
    ],
    deps = [
        ":model",
        ":test_case_parser",
        ":test_case_velocity",
        "//java/jflex/util/javac:package_util",
        "//java/jflex/velocity",
        "//third_party/com/google/common/flogger",
        "//third_party/com/google/guava",
        "//third_party/org/apache/velocity",
    ],
)

java_library(
    name = "test_case_velocity",
    srcs = ["TestCaseVars.java"],
    resources = ["TestCase.java.vm"],
    deps = [
        "//java/jflex/velocity",
        "//third_party/org/apache/velocity",
    ],
)

java_library(
    name = "test_case_parser",
    srcs = [
        ":gen_test_loader",
    ],
    deps = [
        ":model",
        "//jflex/src/main/java/jflex/core",
    ],
)

java_library(
    name = "model",
    srcs = [
        "InputOutput.java",
        "TestCase.java",
    ],
    deps = [
    ],
)

jflex(
    name = "gen_test_loader",
    srcs = ["TestSpecScanner.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["TestSpecScanner.java"],
)
