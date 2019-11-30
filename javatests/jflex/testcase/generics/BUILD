# generics4

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_generics4_scanner",
    srcs = ["generics4.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Generics4.java"],
)

java_library(
    name = "generics4_scanner",
    srcs = [
        ":gen_generics4_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "Generics4GoldenTest",
    srcs = [
        "Generics4GoldenTest.java",
    ],
    data = [
        "generics4-0.input",
        "generics4-0.output",
    ],
    deps = [
        ":generics4_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
