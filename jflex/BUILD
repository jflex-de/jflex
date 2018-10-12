package(default_visibility = ["//visibility:public"])

java_binary(
    name = "jflex_bin",
    main_class = "jflex.Main",
    runtime_deps = [
        ":jflex",
        "//cup:cup_runtime",
    ],
)

java_import(
    name = "jflex",
    jars = ["target/jflex-1.7.1-SNAPSHOT.jar"],
)
