# ccl-bug

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_ccl-bug_scanner",
    srcs = ["ccl-bug.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["CclBug.java"],
)

java_library(
    name = "ccl-bug_scanner",
    srcs = [
        ":gen_ccl-bug_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "CclBugGoldenTest",
    srcs = [
        "CclBugTest.java",
    ],
    data = [
    ],
    deps = [
        ":ccl-bug_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
