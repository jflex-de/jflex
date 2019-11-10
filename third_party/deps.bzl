# Please keep deps in alphabetical order
def third_party_deps():
    native.maven_jar(
        name = "jsr305",
        artifact = "com.google.code.findbugs:jsr305:3.0.2",
        repository = "https://jcenter.bintray.com/",
        sha256 = "766ad2a0783f2687962c8ad74ceecc38a28b9f72a2d085ee438b7813e928d0c7",
    )
    native.maven_jar(
        name = "org_apache_ant_ant",
        artifact = "org.apache.ant:ant:1.7.0",
        repository = "https://jcenter.bintray.com/",
        sha256 = "92f72307e7440f1e352c916f2438d2bbab3ffd2cf730c71316117ad04abadea8",
    )
    native.maven_jar(
        name = "org_apache_commons_collections",
        artifact = "commons-collections:commons-collections:jar:3.2.1",
        repository = "https://jcenter.bintray.com/",
        sha256 = "87363a4c94eaabeefd8b930cb059f66b64c9f7d632862f23de3012da7660047b",
    )
    native.maven_jar(
        name = "org_apache_commons_lang",
        artifact = "commons-lang:commons-lang:jar:2.4",
        repository = "https://jcenter.bintray.com/",
        sha256 = "2c73b940c91250bc98346926270f13a6a10bb6e29d2c9316a70d134e382c873e",
    )
    native.maven_jar(
        name = "org_apache_velocity",
        artifact = "org.apache.velocity:velocity:jar:1.7",
        repository = "https://jcenter.bintray.com/",
        sha256 = "ec92dae810034f4b46dbb16ef4364a4013b0efb24a8c5dd67435cae46a290d8e",
    )
    native.maven_jar(
        name = "com_google_auto_value_auto_value",
        artifact = "com.google.auto.value:auto-value:jar:1.7",
        repository = "https://jcenter.bintray.com/",
        sha256 = "b66df6984d5c29f15a6253514c817fb046e8e242efffc79e42c33f2dde0edf41",
    )
    native.maven_jar(
        name = "com_google_auto_value_auto_value_annotations",
        artifact = "com.google.auto.value:auto-value-annotations:jar:1.7",
        repository = "https://jcenter.bintray.com/",
        sha256 = "b134bab5082e9f49f2b45802573c78e0726e059b645323645da03e328e501f86",
    )
    native.maven_jar(
        name = "com_google_guava_guava",
        artifact = "com.google.guava:guava:jar:26.0-jre",
        repository = "https://jcenter.bintray.com/",
        sha256 = "a0e9cabad665bc20bcd2b01f108e5fc03f756e13aea80abaadb9f407033bea2c",
    )
    native.maven_jar(
        name = "com_google_truth_truth",
        artifact = "com.google.truth:truth:0.36",
        repository = "https://jcenter.bintray.com/",
        sha256 = "aa19c5987eb52a9d7fb954823e8ea70d36308a5bde309c2020abe5e329c17527",
    )
    native.maven_jar(
        name = "junit_junit",
        artifact = "junit:junit:jar:4.12",
        repository = "https://jcenter.bintray.com/",
        sha256 = "59721f0805e223d84b90677887d9ff567dc534d7c502ca903c0c2b17f05c116a",
    )
