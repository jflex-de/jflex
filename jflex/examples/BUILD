package(default_visibility = ["//visibility:public"])

licenses(["notice"])  # BSD

java_binary(
    name = "jflex_bin",
    main_class = "jflex.Main",
    runtime_deps = [":jflex"],
)

# The snapshots are:
# - built by Maven
# - copied in this directort by bazel.sh
java_import(
    name = "jflex",
    jars = ["jflex.jar"],
    deps = [":cup_runtime"],
)

java_import(
    name = "cup_runtime",
    jars = ["cup_runtime.jar"],
)
