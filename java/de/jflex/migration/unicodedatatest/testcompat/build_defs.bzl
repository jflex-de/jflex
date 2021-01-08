def gen_test_compat(name, version, ucd):
    """Generate the Java test and the Scanner spec to test the compatibility properties."""
    underscore_version = version.replace(".", "_")
    flexout = "unicode_{version}/UnicodeCompat_alnum_{version}.flex".format(
        version = underscore_version,
    )
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd],
        outs = [
            "javatests/de/jflex/testcase/unicode/" + flexout,
        ],
        cmd = "$(location generator) {version} $(RULEDIR) $(locations {ucd})".format(
            version = version,
            ucd = ucd,
        ),
        tools = [":generator"],
    )
