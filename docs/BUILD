# Build the user manual

load("@bazel_pandoc//:pandoc.bzl", "pandoc")
load("@bazel_latex//:latex.bzl", "latex_document")
load(":docs.bzl", "jflex_doc_tex", "replace_placeholders")

GENRULE_CONCATENATE = "cat $(SRCS) > $@"

DOC_SECTIONS = [
    "intro",
    "installing",
    "maven-plugin",
    "ant-task",
    "example",
    "lex-specs",
    "encodings",
    "performance",
    "porting-and-parsers",
    "end",
]

MD_SRCS = ["md/head.md"] + ["md/" + src + ".md" for src in DOC_SECTIONS]

TEX_SRCS = [":" + section + "_tex" for section in DOC_SECTIONS]

# TODO(regisd) Add `output` attribute
# https://github.com/ProdriveTechnologies/bazel-pandoc/pull/1
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
    to_format = "html",
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

latex_document(
    name = "manual",
    srcs = [
        ":fig/jflex-black.png",
        ":first_page",
        ":latex_content",
        "@bazel_latex//packages:geometry",
        "@bazel_latex//packages:graphicx",
        "@bazel_latex//packages:hyperref",
        "@bazel_latex//packages:microtype",
        # "@bazel_latex//packages:upquote",
    ] + TEX_SRCS,
    main = "manual_full.tex",
)

replace_placeholders(
    name = "first_page",
    src = "manual_first_page.tex",
    out = "_first_page.tex",
    output_to_bindir = True,  # that's where pdf_latex looks into
)

# Generates :section_tex (with substituted variables)
[jflex_doc_tex(name = section) for section in DOC_SECTIONS]

genrule(
    name = "latex_content",
    srcs = TEX_SRCS,
    outs = ["_content.tex"],
    cmd = "echo '" + ("\n".join(["\\input{docs/" + section + "_tex}" for section in DOC_SECTIONS])) + "' > $@",
    output_to_bindir = True,  # that's where pdf_latex looks into
)
