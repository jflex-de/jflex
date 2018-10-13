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

java_import(
    name = "cup_runtime",
    jars = ["cup_runtime/target/cup_runtime-11b.jar"],
)
