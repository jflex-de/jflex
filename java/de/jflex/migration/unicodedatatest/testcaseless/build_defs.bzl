# Copyright 2021, Google LLC
#
# SPDX-License-Identifier: BSD-2-Clause

def gen_test_caseless(name, version, ucd):
    """Generate the Java test and the Scanner spec to test the Caseless property."""
    underscore_version = version.replace(".", "_")
    flexout = "unicode_{version}/UnicodeCaseless_{version}.flex".format(
        version = underscore_version,
    )
    goldenout = "unicode_{version}/UnicodeCaseless_{version}.output".format(
        version = underscore_version,
    )
    javaout = "unicode_{version}/UnicodeCaselessTest_{version}.java".format(
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
