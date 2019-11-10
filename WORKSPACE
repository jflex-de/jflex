# Workspace file for the Bazel build system
# https://bazel.build/

# JFlex itself is not built with Bazel, but some examples and the documentation are.
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "2.10"
RULES_JVM_EXTERNAL_SHA = "1bbf2e48d07686707dd85357e9a94da775e1dbd7c464272b3664283c9c716d26"
http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

git_repository(
    name = "jflex_rules",
    remote = "https://github.com/jflex-de/bazel_rules.git",
    tag = "v4",
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
#
#http_archive(
#    name = "bazel_latex",
#    sha256 = "b4dd9ae76c570b328be30cdc5ea7045a61ecd55e4e6e2e433fb3bb959be2a44b",
#    strip_prefix = "bazel-latex-0.16",
#    url = "https://github.com/ProdriveTechnologies/bazel-latex/archive/v0.16.tar.gz",
#)
#
# This is a proposed fix for `OSError: [Errno 13] Permission denied: run_lualatex.py`
# https://github.com/ProdriveTechnologies/bazel-latex/issues/23
git_repository(
    name = "bazel_latex",
    commit = "1ba1fb087b8526cfe28c7c31471f412107ee6f09",
    remote = "https://github.com/Selmaai/bazel-latex.git",
)

load("@bazel_latex//:repositories.bzl", "latex_repositories")

latex_repositories()

# Third-party depenencies
load("//third_party:deps.bzl", "third_party_deps")

third_party_deps()

# Unicode character definitions (UCD) from Unicode.org
load("//third_party/unicode:unicode.bzl", "unicode_deps")

unicode_deps()
