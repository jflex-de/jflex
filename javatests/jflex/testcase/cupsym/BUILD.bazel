# cupsym

# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

jflex(
    name = "gen_cupsym_scanner",
    srcs = ["cupsym.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Cupsym.java"],
)

java_library(
    name = "cupsym_scanner",
    srcs = [
        "sym2.java",
        ":gen_cupsym_scanner",
    ],
    deps = [
        "//cup/cup_runtime",
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "CupsymGoldenTest",
    srcs = [
        "CupsymGoldenTest.java",
    ],
    data = [
        "cupsym-0.input",
        "cupsym-0.output",
    ],
    deps = [
        ":cupsym_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
