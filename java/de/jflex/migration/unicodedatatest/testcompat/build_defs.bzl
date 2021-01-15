# Copyright 2021, Google LLC
#
# SPDX-License-Identifier: BSD-2-Clause

def gen_test_compat(name, version, ucd):
    """Generate the Java test and the Scanner spec to test the compatibility properties."""
    underscore_version = version.replace(".", "_")
    outs = [
        "unicode_{version}/UnicodeCompatibilityProperties_alnum_{version}.flex",
        "unicode_{version}/UnicodeCompatibilityProperties_blank_{version}.flex",
        "unicode_{version}/UnicodeCompatibilityProperties_graph_{version}.flex",
        "unicode_{version}/UnicodeCompatibilityProperties_print_{version}.flex",
        "unicode_{version}/UnicodeCompatibilityProperties_xdigit_{version}.flex",
        "unicode_{version}/UnicodeCompatibilityProperties_alnum_{version}.output",
        "unicode_{version}/UnicodeCompatibilityProperties_blank_{version}.output",
        "unicode_{version}/UnicodeCompatibilityProperties_graph_{version}.output",
        "unicode_{version}/UnicodeCompatibilityProperties_print_{version}.output",
        "unicode_{version}/UnicodeCompatibilityProperties_xdigit_{version}.output",
        "unicode_{version}/UnicodeCompatibilityPropertiesTest_{version}.java",
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
