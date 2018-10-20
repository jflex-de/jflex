package(default_visibility = ["//visibility:public"])

licenses(["notice"])  # Apache License 2.0

java_library(
    name = "auto_value",
    exported_plugins = [":auto_plugin"],
    exports = ["@com_google_auto_value_auto_value//jar"],
)

java_plugin(
    name = "auto_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = ["@com_google_auto_value_auto_value//jar"],
)
