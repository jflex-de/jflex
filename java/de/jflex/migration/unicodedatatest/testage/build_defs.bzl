load("//java/de/jflex/migration/unicodedatatest:build_defs.bzl", "KNOWN_VERSIONS")

def gen_test_age(name, version):
    underscore_version = version.replace(".", "_")
    version_tuple = tuple([int(x) for x in version.split(".")])
    older_ages = [v for v in KNOWN_VERSIONS if v <= version_tuple]
    dir = "javatests/de/jflex/testcase/unicode/unicode_{version}".format(
        version = underscore_version,
    )
    age_outputs = [
        "{dir}/UnicodeAge_{version}_age_{major}_{minor}.flex".format(
            dir = dir,
            version = underscore_version,
            major = age[0],
            minor = age[1],
        )
        for age in older_ages
    ]
    age_outputs += [
        "{dir}/UnicodeAge_{version}_age_subtraction.flex".format(
            dir = dir,
            version = underscore_version,
        ),
        "{dir}/UnicodeAge_{version}_age_unassigned.flex".format(
            dir = dir,
            version = underscore_version,
        ),
    ]
    native.genrule(
        name = name,
        testonly = True,
        outs = [
            "{dir}/UnicodeAgeTest_{version}.java".format(
                dir = dir,
                version = underscore_version,
            ),
        ] + age_outputs,
        cmd = "$(location :generator) {version} $(RULEDIR)".format(version = version),
        tools = [":generator"],
    )
