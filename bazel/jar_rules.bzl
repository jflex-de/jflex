def simple_jar(name, srcs):
    """Creates a jar out of a set of flat files."""
    native.genrule(
        name = name,
        srcs = srcs,
        outs = ["%s.jar" % name],
        cmd = """
        OUT="$$(pwd)/$@"
        cd {package_name}
        jar cf "$$OUT" * &> /dev/null
        """.format(package_name = native.package_name()),
    )
