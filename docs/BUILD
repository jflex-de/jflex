# Build the user manual

load("@bazel_pandoc//:pandoc.bzl", "pandoc")

VERSION="1.7.1-SNAPSHOT"
RELEASE_DATE="21 September 2018"
UNICODE_VER="9.0"

pandoc(
    name = "html",
    src = "manual.mdx",
    from_format = "markdown",
    to_format = "html",
)

genrule(
    name = "concatenated_manual",
        srcs=[
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
    cmd = "cat $(SRCS) > $@",  
    outs = ["concatenated_manual.mdx"],
)

