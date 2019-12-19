load("//testsuite:testsuite.bzl", "jflex_testsuite")

jflex_testsuite(
    name = "LineTest",
    srcs = ["LineTest.java"],
    data = [
        "line.flex",
        "line-flex.syserr",
        "line-flex.sysout",
    ],
    deps = [
        "//java/jflex/util/javac",
        "//third_party/com/google/guava",
    ],
)
