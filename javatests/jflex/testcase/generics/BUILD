# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")
# generics

jflex(
    name = "gen_generics_scanner",
    srcs = ["generics.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Generics.java"],
)

java_library(
    name = "generics_scanner",
    srcs = [
        ":gen_generics_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "GenericsGoldenTest",
    srcs = [
        "GenericsGoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":generics_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
# generics2

jflex(
    name = "gen_generics2_scanner",
    srcs = ["generics2.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Generics2.java"],
)

java_library(
    name = "generics2_scanner",
    srcs = [
        ":gen_generics2_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "Generics2GoldenTest",
    srcs = [
        "Generics2GoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":generics2_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
# generics3

jflex(
    name = "gen_generics3_scanner",
    srcs = ["generics3.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Generics3.java"],
)

java_library(
    name = "generics3_scanner",
    srcs = [
        ":gen_generics3_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "Generics3GoldenTest",
    srcs = [
        "Generics3GoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":generics3_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
# generics4

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
