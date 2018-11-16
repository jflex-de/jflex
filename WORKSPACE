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

# TODO(regisd) Take upstream when they have accepted my PR to allow specifying output
# https://github.com/ProdriveTechnologies/bazel-pandoc/pull/1
#http_archive(
#    name = "bazel_pandoc",
#    strip_prefix = "bazel-pandoc-0.1",
#    url = "https://github.com/ProdriveTechnologies/bazel-pandoc/archive/v0.1.tar.gz",
#)
http_archive(
    name = "bazel_pandoc",
    sha256 = "0dd9d0d44658d46a96c36caba25f7ce9f119a6883c3219f61b76c11cfdc83c8f",
    strip_prefix = "bazel_pandoc-0.1.1",
    url = "https://github.com/regisd/bazel_pandoc/archive/v0.1.1.tar.gz",
)

load("@bazel_pandoc//:repositories.bzl", "pandoc_repositories")

pandoc_repositories()

# latex rule to build PDF from tex files

http_archive(
    name = "bazel_latex",
    sha256 = "ecab535bb50699817cea662014432b4398edd7318ee6a6f64399a3287010961c",
    strip_prefix = "bazel-latex-0.9",
    url = "https://github.com/ProdriveTechnologies/bazel-latex/archive/v0.9.tar.gz",
)

load("@bazel_latex//:repositories.bzl", "latex_repositories")

latex_repositories()

# Third-party depenencies
load("//third_party:deps.bzl", "third_party_deps")

third_party_deps()

load("//third_party:maven_workspace.bzl", "maven_dependencies")
maven_dependencies()

# Unicode character definitions (UCD) from Unicode.org
load("//third_party/unicode:unicode.bzl", "unicode_deps")

unicode_deps()
