load("@jflex_rules//jflex:jflex.bzl", "jflex")

java_test(
    name = "LargeInputTest",
    srcs = [
        "LargeInputTest.java",
    ],
    jvm_flags = ["-Xmx5G"],
    deps = [
        ":large_input_scanner",
        ":repeat_content_reader",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)

java_library(
    name = "large_input_scanner",
    srcs = [
        "State.java",
        ":gen_large_input_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

jflex(
    name = "gen_large_input_scanner",
    srcs = ["large_input.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["LargeInputScanner.java"],
)

java_library(
    name = "repeat_content_reader",
    srcs = ["RepeatContentReader.java"],
    deps = [],
)

java_test(
    name = "RepeatContentReaderTest",
    srcs = ["RepeatContentReaderTest.java"],
    deps = [
        ":repeat_content_reader",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
