KNOWN_VERSIONS = ((2, 0), (2, 1), (3, 0), (3, 1), (3, 2), (4, 0), (4, 1), (5, 0), (5, 1), (5, 2), (6, 0), (6, 1), (6, 2), (6, 3), (7, 0), (8, 0), (9, 0), (10, 0), (11, 0), (12, 0), (12, 1))

def gen_build(name, version):
    underscore_version = version.replace(".", "_")
    native.genrule(
        name = name,
        testonly = True,
        outs = ["javatests/de/jflex/testcase/unicode/unicode_{version}/BUILD.bazel".format(
            version = underscore_version,
        )],
        cmd = "$(location :generator) {version} $(RULEDIR)".format(version = version),
        tools = [":generator"],
    )
