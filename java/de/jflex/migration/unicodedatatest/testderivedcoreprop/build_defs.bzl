# Copyright 2021, Google LLC
#
# SPDX-License-Identifier: BSD-2-Clause

def gen_test(name, version, ucd):
    """Generate the Scanner spec to test the derived age core properties."""
    underscore_version = version.replace(".", "_")
    outs = [
        "unicode_{version}/UnicodeDerivedCoreProperties_Alphabetic_{version}.flex",
        "unicode_{version}/UnicodeDerivedCoreProperties_Alphabetic_{version}.output",
        "unicode_{version}/UnicodeDerivedCorePropertiesTest_{version}.java",
    ]
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd],
        outs = ["javatests/de/jflex/testcase/unicode/" +
                out.format(
                    version = underscore_version,
                ) for out in outs],
        cmd = "$(location generator) {version} $(RULEDIR) $(locations {ucd})".format(
            version = version,
            ucd = ucd,
        ),
        tools = [":generator"],
    )
