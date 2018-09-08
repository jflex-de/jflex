alias(
    name = "jflex",
    actual = "//jflex/src/main"
)

alias(
    name = "jflex_1_6_1",
    actual = "@de_jflex_jflex_1_6_1//jar",
)

java_binary(
    name = "jflex_1_6_1_bin",
    main_class = "jflex.Main",
    runtime_deps = [
        ":jflex_1_6_1",
    ],
    visibility = ["//jflex/src/main:__subpackages__"],
)
