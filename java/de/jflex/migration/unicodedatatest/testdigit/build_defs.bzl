# Copyright 2021, Google LLC
#
# SPDX-License-Identifier: BSD-2-Clause

def gen_test_digit(name, version, ucd):
    """Generate the Java test and the Scanner spec to test the digit property."""
    underscore_version = version.replace(".", "_")
    outs = [
        "unicode_{version}/UnicodeDigit_{version}.flex",
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
