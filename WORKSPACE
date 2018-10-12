# Workspace file for the Bazel build system
# https://bazel.build/

# JFlex itself is not built with Bazel, but some examples and the documentation are.
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "jflex_rules",
    branch = "stable",
    remote = "https://github.com/jflex-de/bazel_rules.git",
)

load("@jflex_rules//jflex:deps.bzl", "jflex_deps")

jflex_deps()

# pandoc used to build the documentatoin

# TODO(regisd) Take upstream when they have accepted my PR to allow specifying output
# https://github.com/ProdriveTechnologies/bazel-pandoc/pull/1
#http_archive(
#    name = "bazel_pandoc",
#    strip_prefix = "bazel-pandoc-0.1",
#    url = "https://github.com/ProdriveTechnologies/bazel-pandoc/archive/v0.1.tar.gz",
#)
http_archive(
    name = "bazel_pandoc",
    strip_prefix = "bazel_pandoc-0.1.1",
    url = "https://github.com/regisd/bazel_pandoc/archive/v0.1.1.tar.gz",
    sha256 = "0dd9d0d44658d46a96c36caba25f7ce9f119a6883c3219f61b76c11cfdc83c8f",
)


load("@bazel_pandoc//:repositories.bzl", "pandoc_repositories")

pandoc_repositories()

# latex rule to build PDF from tex files

http_archive(
    name = "bazel_latex",
    strip_prefix = "bazel-latex-0.9",
    url = "https://github.com/ProdriveTechnologies/bazel-latex/archive/v0.9.tar.gz",
)

load("@bazel_latex//:repositories.bzl", "latex_repositories")

latex_repositories()

# Third-party depenencies

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

