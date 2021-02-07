def gen_test_emoji(name, ucd, version):
    underscore_version = version.replace(".", "_")
    outs = [
        "unicode_{version}/UnicodeEmojiTest_{version}.java",
        "unicode_{version}/UnicodeEmoji_Emoji_{version}.flex",
        "unicode_{version}/UnicodeEmoji_Emoji_{version}.output",
        "unicode_{version}/UnicodeEmoji_Emoji_Modifier_{version}.flex",
        "unicode_{version}/UnicodeEmoji_Emoji_Modifier_{version}.output",
        "unicode_{version}/UnicodeEmoji_Emoji_Modifier_Base_{version}.flex",
        "unicode_{version}/UnicodeEmoji_Emoji_Modifier_Base_{version}.output",
        "unicode_{version}/UnicodeEmoji_Emoji_Presentation_{version}.flex",
        "unicode_{version}/UnicodeEmoji_Emoji_Presentation_{version}.output",
    ]
    version_major = int(version.split(".")[0])
    if version_major >= 10:
        outs += [
            "unicode_{version}/UnicodeEmoji_Emoji_Component_{version}.flex",
            "unicode_{version}/UnicodeEmoji_Emoji_Component_{version}.output",
        ]
    if version_major >= 11:
        outs += [
            "unicode_{version}/UnicodeEmoji_Extended_Pictographic_{version}.flex",
            "unicode_{version}/UnicodeEmoji_Extended_Pictographic_{version}.output",
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
