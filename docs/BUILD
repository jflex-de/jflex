# Build the user manual

load("@bazel_pandoc//:pandoc.bzl", "pandoc")
load("@bazel_latex//:latex.bzl", "latex_document")

VERSION = "1.7.1-SNAPSHOT"

RELEASE_DATE = "21 September 2018"

UNICODE_VER = "9.0"

GENRULE_CONCATENATE = "cat $(SRCS) > $@"

pandoc(
    name = "html",
    src = ":manual_mdx",
    from_format = "markdown",
    # TODO: Add "--css manual.css" when the rule accepts data files
    #
    # TODO: Add "+smart" when the toolchain supports it
    #
    # TODO: Add ["--filter", "pandoc-citeproc"] when #2 is fixed
    # https://github.com/ProdriveTechnologies/bazel-pandoc/issues/2
    options = [],
    output = "manual.html",
    to_format = "html",
)

pandoc(
    name = "latex_content",
    src = ":manual_mdx",
    from_format = "markdown",
    output = "content.tex",  # If changed, then change \include{} in manual.tex
    to_format = "latex",
)

latex_document(
    name = "pdf",
    srcs = ["manual.tex", ":latex_content"],
    main = "manual.tex",
)

# Replaces placeholders by their respective value.
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

# Concatenates all pages in a single document.
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
    outs = ["concatenated_manual.md"],
    cmd = GENRULE_CONCATENATE,
)
