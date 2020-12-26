load("//testsuite:testsuite.bzl", "jflex_testsuite")

jflex_testsuite(
    name = "SemcheckTest",
    srcs = [
        "SemcheckTest.java",
    ],
    data = [
        "semcheck.flex",
        "semcheck-flex.output",
    ],
    deps = [
        "//jflex/src/main/java/jflex/exceptions",
    ],
)
