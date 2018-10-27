java_library(
    name = "unicode",
    srcs = glob(
        ["*.java"],
        exclude = ["*Mojo.java"],
    ) + ["//jflex-unicode-maven-plugin/src/main/jflex:gen_ucd_scanners"],
    visibility = ["//java/ucd_generator:__pkg__"],
)
