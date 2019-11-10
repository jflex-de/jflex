load("@rules_jvm_external//:defs.bzl", "maven_install")

# Please keep deps in alphabetical order
def third_party_deps():
    maven_install(
        artifacts = [
            "com.google.code.findbugs:jsr305:3.0.2",
            "org.apache.ant:ant:1.7.0",
            "commons-collections:commons-collections:jar:3.2.1",
            "commons-lang:commons-lang:jar:2.4",
            "org.apache.velocity:velocity:jar:1.7",
            "com.google.auto.value:auto-value:jar:1.7",
            "com.google.auto.value:auto-value-annotations:jar:1.7",
            "com.google.guava:guava:jar:26.0-jre",
            "com.google.truth:truth:0.36",
            "junit:junit:jar:4.12",
        ],
        repositories = [
            "https://jcenter.bintray.com/",
            "https://maven.google.com",
            "https://repo1.maven.org/maven2",
        ],
    )
