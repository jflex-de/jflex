package(default_visibility=["//visibility/public"])

java_library(
    name = "com_google_code_findbugs_jsr305",
    exports = ["@com_google_code_findbugs_jsr305//jar"],
)


java_library(
    name = "org_codehaus_mojo_animal_sniffer_annotations",
    exports = ["@org_codehaus_mojo_animal_sniffer_annotations//jar"],
)


java_library(
    name = "com_google_errorprone_error_prone_annotations",

    exports = ["@com_google_errorprone_error_prone_annotations//jar"],
)


java_library(
    name = "com_google_guava_guava",
    exports = ["@com_google_guava_guava//jar"],
    runtime_deps = [
        ":com_google_code_findbugs_jsr305",
        ":com_google_errorprone_error_prone_annotations",
        ":com_google_j2objc_j2objc_annotations",
        ":org_codehaus_mojo_animal_sniffer_annotations",
    ],
)


java_library(
    name = "com_google_j2objc_j2objc_annotations",
    exports = ["@com_google_j2objc_j2objc_annotations//jar"],
)


