load("@bazel_pandoc//:pandoc.bzl", "pandoc")
load("//docs:docs.bzl", "DOC_SECTIONS", "replace_placeholders")

ALL_SECTIONS = ["head"] + DOC_SECTIONS

GENRULE_CONCATENATE = "cat $(SRCS) > $@"

[replace_placeholders(
    name = section + "_md",
    src = section + ".md",
) for section in ALL_SECTIONS]

# Concatenates all pages in a single document.
genrule(
    name = "concatenated_md",
    srcs = [":%s_md" % section for section in ALL_SECTIONS],
    outs = ["concatenated_manual.md"],
    cmd = GENRULE_CONCATENATE,
    visibility = ["//docs:__pkg__"],
)

# Generates :section_tex (with substituted variables)
[pandoc(
    name = section + "_tex",
    src = ":" + section + "_md",
    from_format = "markdown",
    options = ["--biblatex"],
    to_format = "latex",
    visibility = ["//docs:__pkg__"],
) for section in DOC_SECTIONS]
