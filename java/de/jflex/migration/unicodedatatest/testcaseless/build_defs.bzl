def gen_test_caseless(name, version, ucd):
    """Generate the Java test and the Scanner spec to test the Bock property."""
    underscore_version = version.replace(".", "_")
    flexout = "unicode_{version}/UnicodeCaseless_{version}.flex".format(
        version = underscore_version,
    )
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd],
        outs = [
            "javatests/de/jflex/testcase/unicode/" + flexout,
        ],
        cmd = "$(location generator) {version} $(RULEDIR) external/{ucd}/UnicodeData.txt".format(
            version = version,
            ucd = ucd,
        ),
        tools = [":generator"],
    )
