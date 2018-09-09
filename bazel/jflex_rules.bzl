""" Skylark rules for JFlex. """

def _jflex_impl(ctx):
    """ Generates a Java lexer/paser from a lex definition, using JFlex. """

    # Output directory is bazel-genfiles, regardless of Java package defined in
    # the grammar
    output_dir = "/".join(
        [ctx.configuration.genfiles_dir.path, ctx.label.package],
    )
    # TODO(regisd): Add support for JFlex options.
    maybe_skel =  [ctx.file.skeleton] if ctx.file.skeleton else []
    cmd_maybe_skel = ["-skel", ctx.file.skeleton.path] if ctx.file.skeleton else []
    arguments = (
        cmd_maybe_skel + \
        # Option to specify output directory
        ["-d", output_dir] + \
        # Input files
        [f.path for f in ctx.files.srcs])
    ctx.action(
        inputs = ctx.files.srcs + maybe_skel,
        outputs = ctx.outputs.outputs,
        executable = ctx.executable._jflex,
        arguments =
            arguments,
    )
    print("Arguments " + (" ".join(arguments)))

jflex = rule(
    implementation = _jflex_impl,
    attrs = {
        "srcs": attr.label_list(
            allow_empty = False,
            allow_files = True,
            mandatory = True,
        ),
        "skeleton": attr.label(
            allow_files = True,
            single_file = True,
            doc = "an optional skeleton",
        ),
        "outputs": attr.output_list(allow_empty = False),
        "_jflex": attr.label(
            default = Label("//jflex:jflex_bin"),
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
            doc = "a list of grammar specifications"
        ),
        "skeleton": attr.label(
            allow_files = True,
            single_file = True,
            doc = "an optional skeleton",
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
