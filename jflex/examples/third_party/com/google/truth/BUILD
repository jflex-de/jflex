# Google truth
# Fluent assertions for Java tests
# http://google.github.io/truth/

package(default_visibility = ["//visibility:public"])

licenses(["notice"])  # Apache License 2.0

java_library(
    name = "truth",
    exports = ["@com_google_truth//jar"],
    runtime_deps = [
        ":com_google_code_findbugs_jsr305",
        ":com_google_errorprone_error_prone_annotations",
        ":com_google_guava_guava",
        ":com_google_j2objc_j2objc_annotations",
        ":junit_junit",
        ":org_codehaus_mojo_animal_sniffer_annotations",
    ],
)
