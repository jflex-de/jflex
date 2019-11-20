# Workspace file for the Bazel build system
# https://bazel.build/

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "2.10"

RULES_JVM_EXTERNAL_SHA = "1bbf2e48d07686707dd85357e9a94da775e1dbd7c464272b3664283c9c716d26"

git_repository(
    name = "jflex_rules",
    remote = "https://github.com/jflex-de/bazel_rules.git",
    tag = "v6",
)

load("@jflex_rules//jflex:deps.bzl", "jflex_deps")

jflex_deps()

# pandoc used to build the documentatoin

http_archive(
    name = "bazel_pandoc",
    strip_prefix = "bazel-pandoc-0.2",
    url = "https://github.com/ProdriveTechnologies/bazel-pandoc/archive/v0.2.tar.gz",
sha256="47ad1f08db3e6c8cc104931c11e099fd0603c174400b9cc852e2481abe08db24",
)

load("@bazel_pandoc//:repositories.bzl", "pandoc_repositories")

pandoc_repositories()

# latex rule to build PDF from tex files
http_archive(
    name = "bazel_latex",
    strip_prefix = "bazel-latex-0.19",
    url = "https://github.com/ProdriveTechnologies/bazel-latex/archive/v0.19.tar.gz",
)

load("@bazel_latex//:repositories.bzl", "latex_repositories")

latex_repositories()

# Third-party dependencies
load("//third_party:deps.bzl", "ARTIFACTS")

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    name = "maven",
    artifacts = ARTIFACTS,
    maven_install_json = "//third_party:maven_install.json",
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)

# To update maven_install.json, run this command to re-pin the unpinned repository:
#
#    bazel run @unpinned_maven//:pin
#
load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

# Unicode character definitions (UCD) from Unicode.org
load("//third_party/unicode:unicode.bzl", "unicode_deps")

unicode_deps()
