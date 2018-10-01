package(default_visibility = ["//visibility:public"])

licenses(["notice"])  # BSD

java_binary(
    name = "jflex_bin",
    main_class = "jflex.Main",
    runtime_deps = [":jflex"],
)

# Alias to latest version
alias(
    name = "jflex",
    actual = ":jflex_1_6_1",
)

java_library(
    name = "jflex_1_6_1",
    # @de_jflex_jflex_1_6_1 points to the reference in the WORKSPACE
    exports = ["@de_jflex_jflex_1_6_1//jar"],
    # Only the latest version should be used
    visibility = ["//visibility:private"],
)
