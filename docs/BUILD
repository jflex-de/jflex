# Build the user manual

# Consider using
# load("@bazel_pandoc//:pandoc.bzl", "pandoc")

VERSION = "1.7.1-SNAPSHOT"

RELEASE_DATE = "21 September 2018"

UNICODE_VER = "9.0"


genrule(
    name = "html",
    srcs = ["manual.mdx"],
    cmd = "pandoc --smart --css manual.css --filter pandoc-citeproc -f markdown $< -o $@",
    outs = ["manual.html"],
)

genrule(
    name = "manual_mdx",
    srcs = [":concatenated_manual"],
    outs = ["manual.mdx"],
    cmd = "sed -e 's/\$$VERSION/" + VERSION + "/g'" +
          " -e 's/\$${project.version}/" + VERSION + "/g'" +
          " -e 's/\$$RELEASE_DATE/" + RELEASE_DATE + "/g'" +
          " -e 's/\$$UNICODE_VER/" + UNICODE_VER + "/g'" +
          " $< > $@",
)

genrule(
    name = "concatenated_manual",
    srcs = [
        "md/head.md",
        "md/intro.md",
        "md/installing.md",
        "md/maven-plugin.md",
        "md/ant-task.md",
        "md/example.md",
        "md/lex-specs.md",
        "md/encodings.md",
        "md/performance.md",
        "md/porting-and-parsers.md",
        "md/end.md",
    ],
    outs = ["concatenated_manual.mdx"],
    cmd = "cat $(SRCS) > $@",
)
