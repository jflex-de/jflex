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
