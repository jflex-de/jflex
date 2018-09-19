load("//bazel:cup_rules.bzl", "cup")
load("//bazel:jflex_rules.bzl", "jflex_old")

package(default_visibility = ["//jflex:__subpackages__"],)

exports_files(["skeleton.nested"])

cup(
    name = "gen_parser",
    srcs = ["LexParse.cup"],
    parser = "LexParse",
    symbols = "sym",
    interface = True,
)

jflex_old(
    name = "gen_scanner",
    srcs = ["LexScan.flex"],
    outputs = ["LexScan.java"],
    skeleton = "skeleton.nested",
    
)
