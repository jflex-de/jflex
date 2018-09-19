package(default_visibility = ["//visibility:public"])

licenses(["notice"])

alias(
    name = "java_cup",
    actual = "//third_party/java_cup/11b",
    visibility = ["//visibility:public"],
)

java_binary(
    name = "cup_bin",
    main_class = "java_cup.Main",
    runtime_deps = [":java_cup"],
)
