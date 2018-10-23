java_library(
    name = "plexus_cipher",
    exports = [
        "//external:jar/org/sonatype/plexus/plexus_cipher"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "plexus_sec_dispatcher",
    exports = [
        "//external:jar/org/sonatype/plexus/plexus_sec_dispatcher"
    ],
    runtime_deps = [
        "//3rdparty/jvm/org/codehaus/plexus:plexus_utils",
        ":plexus_cipher"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


