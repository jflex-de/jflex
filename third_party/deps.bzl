# Please keep deps in alphabetical order
def third_party_deps():
    # @Nullable
    native.maven_jar(
        name = "jsr305",
        artifact = "com.google.code.findbugs:jsr305:3.0.2",
        repository = "https://jcenter.bintray.com/",
        sha1 = "25ea2e8b0c338a877313bd4672d3fe056ea78f0d",
    )
    native.maven_jar(
        name = "org_apache_ant_ant",
        artifact = "org.apache.ant:ant:1.7.0",
        repository = "https://jcenter.bintray.com/",
        sha1 = "9746af1a485e50cf18dcb232489032a847067066",
    )
    native.maven_jar(
        name = "org_apache_commons_collections",
        artifact = "commons-collections:commons-collections:jar:3.2.1",
        repository = "https://jcenter.bintray.com/",
        sha1 = "761ea405b9b37ced573d2df0d1e3a4e0f9edc668",
    )
    native.maven_jar(
        name = "org_apache_commons_lang",
        artifact = "commons-lang:commons-lang:jar:2.4",
        repository = "https://jcenter.bintray.com/",
        sha1 = "16313e02a793435009f1e458fa4af5d879f6fb11",
    )
    native.maven_jar(
        name = "org_apache_maven_plugin_tools_maven_plugin_annotations",
        artifact = "org.apache.maven.plugin-tools:maven-plugin-annotations:3.5.2",
        repository = "https://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "org_apache_maven_maven_plugin_api",
        artifact = "org.apache.maven:maven-plugin-api:3.5.4",
        repository = "https://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "org_apache_maven_plugin_testing_maven_plugin_testing_harness",
        artifact = "org.apache.maven.plugin-testing:maven-plugin-testing-harness:jar:3.3.0",
        repository = "https://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "org_apache_maven_maven_project",
        artifact = "org.apache.maven:maven-project:2.2.1",
        repository = "https://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "org_apache_velocity",
        artifact = "org.apache.velocity:velocity:jar:1.7",
        repository = "https://jcenter.bintray.com/",
        sha1 = "2ceb567b8f3f21118ecdec129fe1271dbc09aa7a",
    )
    native.maven_jar(
        name = "com_google_auto_value_auto_value",
        artifact = "com.google.auto.value:auto-value:jar:1.4.1",
        repository = "http://jcenter.bintray.com/",
    )

    # Guava 21 or above requires JDK 8, but we want to keep running on JDK 7.
    native.maven_jar(
        name = "com_google_guava_guava",
        artifact = "com.google.guava:guava:jar:20.0",
        repository = "http://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "com_google_truth_truth",
        artifact = "com.google.truth:truth:0.36",
        repository = "http://jcenter.bintray.com/",
    )

    native.maven_jar(
        name = "junit_junit",
        artifact = "junit:junit:jar:4.12",
        repository = "http://jcenter.bintray.com/",
    )
