# Please keep deps in alphabetical order
def third_party_deps():
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
        name = "com_google_auto_value_auto_value",
        artifact = "com.google.auto.value:auto-value:jar:1.4.1",
        repository = "http://jcenter.bintray.com/",
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
    native.maven_jar(
        name = "junit_junit",
        artifact = "junit:junit:jar:4.12",
        repository = "http://jcenter.bintray.com/",
    )
