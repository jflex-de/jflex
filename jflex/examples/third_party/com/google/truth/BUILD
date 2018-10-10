# Google truth
# Fluent assertions for Java tests
# http://google.github.io/truth/

licenses(["notice"])  # Apache License 2.0

java_library(
    name = "truth",
    visibility = ["//visibility:public"],
    exports = [
        "@com_google_truth_truth//jar",
        # TODO(regisd) This should be a runtime_deps
        # But in tht case Optional is not found
        "//third_party/com/google/guava",
    ],
)
