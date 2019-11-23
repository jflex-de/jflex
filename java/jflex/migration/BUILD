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
        ":test_case_parser",
        "//third_party/com/google/common/flogger",
        "//third_party/com/google/guava",
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
