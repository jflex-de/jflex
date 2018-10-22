# Please keep deps in alphabetical order
def third_party_deps():
    native.maven_jar(
        name = "org_apache_ant_ant",
        artifact = "org.apache.ant:ant:1.7.0",
        repository = "https://jcenter.bintray.com/",
        sha1 = "9746af1a485e50cf18dcb232489032a847067066",
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
        name = "org_apache_maven_maven_project",
        artifact = "org.apache.maven:maven-project:2.2.1",
        repository = "https://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "com_google_guava_guava",
        artifact = "com.google.guava:guava:jar:26.0-jre",
        repository = "http://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "com_google_truth_truth",
        artifact = "com.google.truth:truth:0.36",
        repository = "http://jcenter.bintray.com/",
    )
    # @Nullable
    native.maven_jar(
        name = "jsr305",
        artifact = "com.google.code.findbugs:jsr305:jar:3.0.2",
        repository = "http://jcenter.bintray.com/",
    )
    native.maven_jar(
        name = "junit_junit",
        artifact = "junit:junit:jar:4.12",
        repository = "http://jcenter.bintray.com/",
    )
