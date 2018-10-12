#
# For your own project, please see https://jflex-de.github.io/bazel_rules/
#
# This workspace is mostly to test that the examples build with the SNAPSHOT version of JFLex.
#

# JFlex rule, used by examples
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "jflex_rules",
    branch = "stable",
    remote = "https://github.com/jflex-de/bazel_rules.git",
)

load("@jflex_rules//jflex:deps.bzl", "jflex_deps")

jflex_deps()

# pandoc used to build the documentatoin

http_archive(
    name = "bazel_pandoc",
    strip_prefix = "bazel-pandoc-0.1",
    url = "https://github.com/ProdriveTechnologies/bazel-pandoc/archive/v0.1.tar.gz",
)


load("@bazel_pandoc//:repositories.bzl", "pandoc_repositories")

pandoc_repositories()


# third-party libraries

maven_jar(
    name = "com_google_truth_truth",
    artifact = "com.google.truth:truth:0.36",
    repository = "http://jcenter.bintray.com/",
)

maven_jar(
    name = "com_google_guava_guava",
    artifact = "com.google.guava:guava:jar:26.0-jre",
    repository = "http://jcenter.bintray.com/",
)

