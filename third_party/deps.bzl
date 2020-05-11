# Please keep deps in alphabetical order
# After a change in deps, run this command to re-pin the unpinned repository:
#
#    bazel run @unpinned_maven//:pin
#
ARTIFACTS = [
    "com.google.code.findbugs:jsr305:3.0.2",
    "org.apache.ant:ant:1.7.0",
    "commons-collections:commons-collections:jar:3.2.1",
    "commons-lang:commons-lang:jar:2.4",
    "org.apache.velocity:velocity:jar:1.7",
    "com.google.auto.value:auto-value:jar:1.7",
    "com.google.auto.value:auto-value-annotations:jar:1.7",
    "com.google.flogger:flogger:jar:0.4",
    "com.google.flogger:flogger-system-backend:0.4",
    "com.google.guava:guava:jar:26.0-jre",
    "com.google.truth:truth:0.36",
    "com.pholser:junit-quickcheck-core:0.9",
    "com.pholser:junit-quickcheck-generators:0.9",
    "javax.annotation:jsr250-api:1.0",
    "junit:junit:jar:4.12",
    "com.github.vbmacher:java-cup:jar:11b-20160615",
    "com.github.vbmacher:java-cup-runtime:jar:11b-20160615",
]
