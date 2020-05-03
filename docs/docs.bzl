"""Helpers to build the docs."""

load("@bazel_pandoc//:pandoc.bzl", "pandoc")

VERSION = "1.8.2"

RELEASE_DATE = "21 September 2018"

UNICODE_VER = "12.0"

DOC_SECTIONS = [
    "intro",
    "installing",
    "maven-plugin",
    "ant-task",
    "example",
    "lex-specs",
    "generated-class",
    "encodings",
    "performance",
    "porting-and-parsers",
    "end",
]

def replace_placeholders(name, src = "", out = None, **kwargs):
    """Replaces placeholders by their respective value."""
    if not out:
        out = name + "_VERSIONED.md"
    native.genrule(
        name = name,
        srcs = [src],
        outs = [out],
        cmd = "sed -e 's/\$$VERSION/" + VERSION + "/g'" +
              " -e 's/\$${project.version}/" + VERSION + "/g'" +
              " -e 's/\$$RELEASE_DATE/" + RELEASE_DATE + "/g'" +
              " -e 's/\$$UNICODE_VER/" + UNICODE_VER + "/g'" +
              " $< > $@",
        **kwargs
    )
