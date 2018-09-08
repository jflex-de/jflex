""" Skylark rules for JFlex. """

def _jflex_impl(ctx):
    """ Generates a Java lexer/paser from a lex definition, using JFlex. """

    # Output directory is bazel-genfiles, regardless of Java package defined in
    # the grammar
    output_dir = "/".join(
        [ctx.configuration.genfiles_dir.path, ctx.label.package],
    )
    ctx.action(
        inputs = ctx.files.srcs,
        outputs = ctx.outputs.outputs,
        executable = ctx.executable._jflex,
        arguments =
            # TODO(regisd): Add support for JFlex options.
            # Option to specify output directory
            ["-d", output_dir] +
            # Input files
            [f.path for f in ctx.files.srcs],
    )

jflex = rule(
    implementation = _jflex_impl,
    attrs = {
        "srcs": attr.label_list(
            allow_empty = False,
            allow_files = True,
            mandatory = True,
        ),
        "outputs": attr.output_list(allow_empty = False),
        "_jflex": attr.label(
            default = Label("//third_party/java/jflex"),
            executable = True,
            cfg = "host",
        ),
    },
    output_to_genfiles = True,  # JFlex generates java files, not bin files
)

# Use the previous version of JFlex.
# This is for bootstraping the compilation of the current version.
jflex_old = rule(
    implementation = _jflex_impl,
    attrs = {
        "srcs": attr.label_list(
            allow_empty = False,
            allow_files = True,
            mandatory = True,
        ),
        "outputs": attr.output_list(allow_empty = False),
        "_jflex": attr.label(
            default = Label("//jflex:jflex_1_6_1_bin"),
            executable = True,
            cfg = "host",
        ),
    },
    output_to_genfiles = True,  # JFlex generates java files, not bin files
)
