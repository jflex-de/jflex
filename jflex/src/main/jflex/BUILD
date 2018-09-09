load("//bazel:jflex_rules.bzl", "jflex_old")

jflex_old(
    name = "gen_scanner",
    srcs = ["LexScan.flex"],
    outputs = ["LexScan.java"],
    skeleton = "skeleton.nested",
    visibility = ["//jflex/src/main:__pkg__"],
)
