# Build the user manual

load("@bazel_pandoc//:pandoc.bzl", "pandoc")
load("@bazel_latex//:latex.bzl", "latex_document")
load(":docs.bzl", "replace_placeholders")

GENRULE_CONCATENATE = "cat $(SRCS) > $@"

MD_SRCS = [
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
]

pandoc(
    name = "html",
    src = ":manual_md",
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

latex_document(
    name = "manual",
    srcs = [
        "//docs:fig/jflex-black.png",
        "@bazel_latex//packages:graphicx",
        "@bazel_latex//packages:hyperref",
        "@bazel_latex//packages:microtype",
        # "@bazel_latex//packages:upquote",
    ],
    main = "manual_full.tex",
)

genrule(
    name = "manual_full_tex",
    srcs = [
        "manual_start.tex",
        ":latex_content",
        "manual_end.tex",
    ],
    outs = ["manual_full.tex"],
    cmd = GENRULE_CONCATENATE,
)

pandoc(
    name = "latex_content",
    src = ":manual_md",
    from_format = "markdown",
    output = "content.tex",  # If changed, then change \include{} in manual.tex
    to_format = "latex",
)

replace_placeholders(
    name = "manual_md",
    src = ":concatenated_manual",
)


# Concatenates all pages in a single document.
genrule(
    name = "concatenated_manual",
    srcs = MD_SRCS,
    outs = ["concatenated_manual.md"],
    cmd = GENRULE_CONCATENATE,
)
