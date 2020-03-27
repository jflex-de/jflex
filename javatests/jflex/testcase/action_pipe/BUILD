load("//testsuite:testsuite.bzl", "jflex_testsuite")

jflex_testsuite(
    name = "ActionPipeEofTest",
    srcs = ["ActionPipeEofTest.java"],
    data = [
        "action-pipe-eof.flex",
    ],
    deps = ["//jflex/src/main/java/jflex/exceptions"],
)

jflex_testsuite(
    name = "EofPipeActionTest",
    srcs = ["EofPipeActionTest.java"],
    data = [
        "eof-pipe-action.flex",
    ],
    deps = [
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/scanner",
    ],
)
