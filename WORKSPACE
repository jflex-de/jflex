# Workspace file for the Bazel build system
# https://bazel.build/

# JFlex itself is not built with Bazel, but some examples and the documentation do.

# TODO(#390) Add maven 1.7.0 when cup_runtime is published
# de.jflex:jflex-maven-plugin:maven-plugin:1.6.1
maven_jar(
    name = "de_jflex_jflex_1_6_1",
    artifact = "de.jflex:jflex:1.6.1",
    repository = "https://jcenter.bintray.com/",
    sha1 = "eb4d51e1a8ea7ee96068905ddeceb9b28737c7eb",
)

load("//third_party:generate_workspace.bzl", "generated_maven_jars")

generated_maven_jars()

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
