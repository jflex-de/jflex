def simple_jar(name, srcs, visibility = "//visibility:public"):
    """Creates a jar out of a set of flat files."""
    native.genrule(
        name = name,
        srcs = srcs,
        outs = ["%s.jar" % name],
        cmd = """
        OUT="$$(pwd)/$@"
        cd {package_name}
        zip "$$OUT" -r * &> /dev/null
        """.format(package_name = native.package_name()),
        visibility = visibility,
    )
