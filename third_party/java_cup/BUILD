package(default_visibility = ["//visibility:public"])

licenses(["notice"])

# The full java_cup
alias(
    name = "java_cup",
    actual = "//third_party/java_cup/11b:11b",
    visibility = ["//visibility:public"],
)

# ONly the runtime
alias(
    name = "java_cup_runtime",
    actual = "//third_party/java_cup/11b:11b-runtime",
    visibility = ["//visibility:public"],
)

java_binary(
    name = "cup_bin",
    main_class = "java_cup.Main",
    runtime_deps = [":java_cup"],
)

