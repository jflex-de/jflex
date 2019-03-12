"""Helpers to build the docs."""

load("@bazel_pandoc//:pandoc.bzl", "pandoc")

VERSION = "1.8.0-SNAPSHOT"

RELEASE_DATE = "21 September 2018"

UNICODE_VER = "12.0"

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

def jflex_doc_tex(name, src = None):
    """Generates the tex for a section"""
    if not src:
        src = "md/" + name + ".md"
    pandoc(
        name = name + "_tex",
        src = ":" + name + "_md",
        from_format = "markdown",
        to_format = "latex",
        options = ["--biblatex"],
    )

    replace_placeholders(
        name = name + "_md",
        src = src,
    )
