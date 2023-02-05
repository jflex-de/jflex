# Copyright 2021, Régis Décamps
# SPDX-License-Identifier: BSD-3-Clause
#
# Workspace file for the Bazel build system
# https://bazel.build/

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "2.10"

RULES_JVM_EXTERNAL_SHA = "1bbf2e48d07686707dd85357e9a94da775e1dbd7c464272b3664283c9c716d26"

http_archive(
    name = "jflex_rules",
    sha256 = "488e523bfed0e1793c68264341bc9c3050f4de3e4aa920f9a72d76ae327935e6",
    strip_prefix = "bazel_rules-1.9.0",
    url = "https://github.com/jflex-de/bazel_rules/archive/v1.9.0.tar.gz",
)

load("@jflex_rules//jflex:deps.bzl", "JFLEX_ARTIFACTS")

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
    artifacts = ARTIFACTS + JFLEX_ARTIFACTS,
    maven_install_json = "//third_party:maven_install.json",
    repositories = [
        "https://jcenter.bintray.com/",
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

# Unicode character definitions (UCD) from Unicode.org
load("//third_party/unicode:unicode.bzl", "unicode_deps")

unicode_deps()
