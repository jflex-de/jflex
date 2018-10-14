package(default_visibility = ["//visibility:public"])

licenses(["notice"])  # GPL-compatible

java_binary(
    name = "cup_bin",
    main_class = "java_cup.Main",
    runtime_deps = [
        ":cup",
    ],
)

# This is the full Java CUP (with runtime)
java_import(
    name = "cup",
    jars = ["cup/java-cup-11b.jar"],
)

alias(
    name = "cup_runtime",
    actual = "//cup/cup_runtime",
)
