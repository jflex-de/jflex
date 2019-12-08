# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")

# macro

jflex(
    name = "gen_macro_scanner",
    srcs = ["macro.flex"],
    jflex_bin = "//jflex:jflex_bin",
    outputs = ["Macro.java"],
)

java_library(
    name = "macro_scanner",
    srcs = [
        ":gen_macro_scanner",
    ],
    deps = [
        "//java/jflex/testing/testsuite/golden",
        "//third_party/com/google/guava",
    ],
)

java_test(
    name = "MacroGoldenTest",
    srcs = [
        "MacroGoldenTest.java",
    ],
    data = [
    ],
    deps = [
        ":macro_scanner",
        "//java/jflex/testing/diff",
        "//java/jflex/testing/testsuite/golden",
        "//java/jflex/util/scanner:scanner_factory",
        "//third_party/com/google/guava",
        "//third_party/com/google/truth",
    ],
)
