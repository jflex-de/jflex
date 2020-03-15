load("//testsuite:testsuite.bzl", "jflex_testsuite")

jflex_testsuite(
    name = "ApiPrivateTest",
    srcs = ["ApiPrivateTest.java"],
    data = [
        "AttemptPrivateAccess.java",
        "private.flex",
    ],
    deps = [
        "//java/jflex/util/javac",
        "//third_party/com/google/guava",
    ],
)
