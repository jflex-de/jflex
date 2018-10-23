licenses(["notice"])
java_library(
    name = "plexus_cipher",
    exports = [
        "//external:jar/org/sonatype/plexus/plexus_cipher"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "plexus_sec_dispatcher",
    exports = [
        "//external:jar/org/sonatype/plexus/plexus_sec_dispatcher"
    ],
    runtime_deps = [
        "//third_party/jvm/org/codehaus/plexus:plexus_utils",
        ":plexus_cipher"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)


