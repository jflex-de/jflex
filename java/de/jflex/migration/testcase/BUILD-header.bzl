# This build file was generated automatically, but won't be re-generated.
# Feel free to improve.

load("@jflex_rules//jflex:jflex.bzl", "jflex")
load("//scripts:check_deps.bzl", "check_deps")

check_deps(
    name = "deps_to_bootstrap_jflex_test",
    prohibited = "@jflex_rules//jflex:jflex_bin",
)
