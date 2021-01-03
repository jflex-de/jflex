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
