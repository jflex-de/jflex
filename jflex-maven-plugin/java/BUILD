filegroup(
    name = "meta_inf",
    srcs = glob(["META-INF/**"]) + [":copy_pom"],
    visibility = ["//jflex-maven-plugin/src/main/java/jflex/maven/plugin/jflex:__pkg__"],
)
genrule(
    name="copy_pom",
    cmd = "cp $< $@",
    outs = ["META-INF/maven/de.jflex/jflex-maven-plugin/pom.xml"],
    srcs = ["//jflex-maven-plugin:pom.xml"]
)
