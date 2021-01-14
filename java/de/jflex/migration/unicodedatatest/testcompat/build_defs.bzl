def gen_test_compat(name, version, ucd):
    """Generate the Java test and the Scanner spec to test the compatibility properties."""
    underscore_version = version.replace(".", "_")
    flexout = "unicode_{version}/UnicodeCompatibilityProperties_alnum_{version}.flex".format(
        version = underscore_version,
    )
    goldenout = "unicode_{version}/UnicodeCompatibilityProperties_alnum_{version}.output".format(
        version = underscore_version,
    )
    javaout = "unicode_{version}/UnicodeCompatibilityPropertiesTest_{version}.java".format(
        version = underscore_version,
    )
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd],
        outs = [
            "javatests/de/jflex/testcase/unicode/" + flexout,
            "javatests/de/jflex/testcase/unicode/" + goldenout,
            "javatests/de/jflex/testcase/unicode/" + javaout,
        ],
        cmd = "$(location generator) {version} $(RULEDIR) $(locations {ucd})".format(
            version = version,
            ucd = ucd,
        ),
        tools = [":generator"],
    )
