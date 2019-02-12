# Workspace file for the Bazel build system
# https://bazel.build/

# JFlex itself is not built with Bazel, but some examples and the documentation are.
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

git_repository(
    name = "jflex_rules",
    remote = "https://github.com/jflex-de/bazel_rules.git",
    tag = "v3",
)

load("@jflex_rules//jflex:deps.bzl", "jflex_deps")

jflex_deps()

# pandoc used to build the documentatoin

http_archive(
    name = "bazel_pandoc",
    strip_prefix = "bazel-pandoc-0.2",
    url = "https://github.com/ProdriveTechnologies/bazel-pandoc/archive/v0.2.tar.gz",
)

load("@bazel_pandoc//:repositories.bzl", "pandoc_repositories")

pandoc_repositories()

# latex rule to build PDF from tex files

http_archive(
    name = "bazel_latex",
    sha256 = "b4dd9ae76c570b328be30cdc5ea7045a61ecd55e4e6e2e433fb3bb959be2a44b",
    strip_prefix = "bazel-latex-0.16",
    url = "https://github.com/ProdriveTechnologies/bazel-latex/archive/v0.16.tar.gz",
)

load("@bazel_latex//:repositories.bzl", "latex_repositories")

latex_repositories()

# Third-party depenencies
load("//third_party:deps.bzl", "third_party_deps")

third_party_deps()

# Unicode character definitions (UCD) from Unicode.org
load("//third_party/unicode:unicode.bzl", "unicode_deps")

unicode_deps()
