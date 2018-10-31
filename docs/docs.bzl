"""Helpers to build the docs."""

VERSION = "1.7.1-SNAPSHOT"

RELEASE_DATE = "21 September 2018"

UNICODE_VER = "9.0"

def replace_placeholders(name, src = ""):
    """Replaces placeholders by their respective value."""
    native.genrule(
        name = name,
        srcs = [src],
        outs = [name + "_VERSIONED.md"],
        cmd = "sed -e 's/\$$VERSION/" + VERSION + "/g'" +
              " -e 's/\$${project.version}/" + VERSION + "/g'" +
              " -e 's/\$$RELEASE_DATE/" + RELEASE_DATE + "/g'" +
              " -e 's/\$$UNICODE_VER/" + UNICODE_VER + "/g'" +
              " $< > $@",
    )
