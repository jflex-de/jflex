# Copyright 2021, Google LLC
#
# SPDX-License-Identifier: BSD-2-Clause

def gen_test_old_blocks(name, version, ucd_file):
    underscore_version = version.replace(".", "_")
    flexout = "unicode_{version}/UnicodeBlocks_{version}.flex".format(
        version = underscore_version,
    )
    javaout = "unicode_{version}/UnicodeBlocksTest_{version}.java".format(
        version = underscore_version,
    )
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd_file],
        outs = [
            "javatests/de/jflex/testcase/unicode/" + flexout,
            "javatests/de/jflex/testcase/unicode/" + javaout,
        ],
        cmd = "$(location generator) {version} $(RULEDIR) $(location {ucd_file})".format(
            version = version,
            ucd_file = ucd_file,
        ),
        tools = [":generator"],
    )

def gen_test_blocks(name, version, ucd):
    """Generate the Java test and the Scanner spec to test the Bock property."""
    underscore_version = version.replace(".", "_")
    flexout = "unicode_{version}/UnicodeBlocks_{version}.flex".format(
        version = underscore_version,
    )
    javaout = "unicode_{version}/UnicodeBlocksTest_{version}.java".format(
        version = underscore_version,
    )
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd],
        outs = [
            "javatests/de/jflex/testcase/unicode/" + flexout,
            "javatests/de/jflex/testcase/unicode/" + javaout,
        ],
        cmd = "$(location generator) {version} $(RULEDIR) $(locations {ucd})".format(
            version = version,
            ucd = ucd,
        ),
        tools = [":generator"],
    )
