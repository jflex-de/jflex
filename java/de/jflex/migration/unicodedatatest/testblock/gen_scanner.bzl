def gen_old_block_scanner(name, version, ucd_file):
    underscore_version = version.replace(".", "_")
    out = "unicode_{version}/UnicodeBlocks_{version}.flex".format(
        version = underscore_version,
    )
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd_file],
        outs = ["javatests/de/jflex/testcase/unicode/" + out],
        cmd = "$(location generator) {version} $(RULEDIR) $(location {ucd_file})".format(
            version = version,
            ucd_file = ucd_file,
        ),
        tools = [":generator"],
    )

def gen_block_scanner(name, version, ucd):
    underscore_version = version.replace(".", "_")
    out = "unicode_{version}/UnicodeBlocks_{version}.flex".format(
        version = underscore_version,
    )
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd],
        outs = ["javatests/de/jflex/testcase/unicode/" + out],
        cmd = "$(location generator) {version} $(RULEDIR) $(locations {ucd})/Block.txt".format(
            version = version,
            ucd = ucd,
        ),
        tools = [":generator"],
    )
