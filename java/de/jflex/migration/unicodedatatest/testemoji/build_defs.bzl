def gen_test_emoji(name, ucd, version):
    underscore_version = version.replace(".", "_")
    outs = [
        "unicode_{version}/UnicodeEmoji_{version}.flex",
        "unicode_{version}/UnicodeEmojiTest_{version}.java",
        "unicode_{version}/UnicodeEmoji_{version}.output",
    ]
    native.genrule(
        name = name,
        testonly = True,
        srcs = [ucd],
        outs = ["javatests/de/jflex/testcase/unicode/" +
                out.format(
                    version = underscore_version,
                ) for out in outs],
        cmd = "$(location EmojiTestGenerator) {version} $(RULEDIR) $(locations {ucd})".format(
            version = version,
            ucd = ucd,
        ),
        tools = ["//java/de/jflex/migration/unicodedatatest/testemoji:EmojiTestGenerator"],
        visibility = ["//javatests/de/jflex/testcase/unicode:__subpackages__"],
    )
