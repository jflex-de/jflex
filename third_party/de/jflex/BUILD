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
    actual = ":jflex_SNAPSHOT",
)

alias(
    name = "cup_runtime",
    actual = ":cup_runtime_SNAPSHOT",
)

# The snapshot is
# - buitl by Maven
# - copied in this directort by bazel.sh
# It is not necessarily
java_import(
    name = "jflex_SNAPSHOT",
    jars = ["jflex.jar"],
    deps = [":cup_runtime"],
)

java_import(
    name = "cup_runtime_SNAPSHOT",
    jars = ["cup_runtime.jar"],
)

# TODO(#390) Replace old version by jflex-1.7.0 when all deps have been published on central repo.
java_library(
    name = "jflex_1_6_1",
    # Only the latest version should be used
    visibility = ["//visibility:private"],
    # @de_jflex_jflex_1_6_1 points to the reference in the WORKSPACE
    exports = ["@de_jflex_jflex_1_6_1//jar"],
)
