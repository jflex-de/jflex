licenses(["notice"])
java_library(
    name = "maven_resolver_api",
    exports = [
        "//external:jar/org/apache/maven/resolver/maven_resolver_api"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_resolver_impl",
    exports = [
        "//external:jar/org/apache/maven/resolver/maven_resolver_impl"
    ],
    runtime_deps = [
        ":maven_resolver_api",
        ":maven_resolver_spi",
        ":maven_resolver_util"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_resolver_spi",
    exports = [
        "//external:jar/org/apache/maven/resolver/maven_resolver_spi"
    ],
    runtime_deps = [
        ":maven_resolver_api"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_resolver_util",
    exports = [
        "//external:jar/org/apache/maven/resolver/maven_resolver_util"
    ],
    runtime_deps = [
        ":maven_resolver_api"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)


