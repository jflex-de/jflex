package(default_visibility = ["//visibility:public"])

load("@jflex_rules//cup:cup.bzl", "cup")
load("@jflex_rules//jflex:jflex.bzl", "jflex")

package_group(
    name = "visibility",
    packages = ["//jflex/..."],
)

java_binary(
    name = "jflex_bin",
    main_class = "jflex.Main",
    runtime_deps = [
        "//jflex/src/main/java/jflex:jflex_ui",
        "//cup:cup_runtime",
    ],
)

alias(
    name = "jflex",
    actual = "//jflex/src/main/java/jflex",
    srcs = glob(["src/main/java/**/*.java"]) + [
        ":gen_parser",
        ":gen_scanner",
    ],
    resources = glob(["src/main/resources/**"]),
    visibility = ["//visibility:public"],
    deps = [
        "//cup:cup_runtime",
        "//jflex/src/main/java/jflex/exceptions",
        "//jflex/src/main/java/jflex/performance",
        "//third_party/org/apache/ant",
    ],
)

cup(
    name = "gen_parser",
    src = "src/main/cup/LexParse.cup",
    cup_bin = "@jflex_rules//cup:cup_bin",
    interface = True,
    parser = "LexParse",
    symbols = "sym",
)

# Use bootstrap version defined in @jflex_rules
jflex(
    name = "gen_scanner",
    srcs = ["src/main/jflex/LexScan.flex"],
    outputs = ["LexScan.java"],
    skeleton = "src/main/jflex/skeleton.nested",
)

filegroup(
    name = "resources",
    srcs = glob(["src/main/resources/**"]),
)

# Test helpers
alias(
    name = "testing",
    actual = "//jflex/src/test/java/jflex/testing",
)

# Tests
filegroup(
    name = "test_data",
    srcs = glob(["src/test/resources/**"]) + ["src/main/jflex/skeleton.nested"],
)
