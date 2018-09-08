package(default_visibility = ["//visibility:public"])

alias(
    name = "jflex",
    actual = "//jflex/src/main"
)

java_binary(
    name = "jflex_bin",
    runtime_deps = [
        ":jflex",
    ],
    main_class = "jflex.Main",
)

java_binary(
    name = "jflex_gui_bin",
    runtime_deps = [
        ":jflex",
        "//jflex/src/main/java/jflex/gui",
    ],
    main_class = "jflex.gui.Gui",
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
