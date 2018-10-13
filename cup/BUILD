package(default_visibility = ["//visibility:public"])

licenses(["notice"])  # GPL-compatible

java_binary(
    name = "cup_bin",
    main_class = "java_cup.Main",
    runtime_deps = [
        ":cup",
        ":cup_runtime",
    ],
)

java_import(
    name = "cup",
    jars = ["cup/target/cup-11b.jar"],
)

alias(
    name = "cup_runtime",
    actual = "//cup/cup_runtime",
)
