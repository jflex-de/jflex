load("//java/de/jflex/migration/unicodedatatest:build_defs.bzl", "KNOWN_VERSIONS")

def gen_test_age(name, version):
    underscore_version = version.replace(".", "_")
    version_tuple = tuple([int(x) for x in version.split(".")])
    older_ages = [v for v in KNOWN_VERSIONS if v <= version_tuple]
    age_outputs = [
        "javatests/de/jflex/testcase/unicode/unicode_{version}/UnicodeAge_{version}_age_{major}_{minor}.flex".format(
            version = underscore_version,
            major = age[0],
            minor = age[1],
        )
        for age in older_ages
    ]
    native.genrule(
        name = name,
        testonly = True,
        outs = ["javatests/de/jflex/testcase/unicode/unicode_{version}/UnicodeAgeTest_{version}.java".format(
            version = underscore_version,
        )] + age_outputs,
        cmd = "$(location :generator) {version} $(RULEDIR)".format(version = version),
        tools = [":generator"],
    )
