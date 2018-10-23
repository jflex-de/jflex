licenses(["notice"])
java_library(
    name = "maven_artifact",
    exports = [
        "//external:jar/org/apache/maven/maven_artifact"
    ],
    runtime_deps = [
        "//third_party/jvm/org/apache/commons:commons_lang3",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_builder_support",
    exports = [
        "//external:jar/org/apache/maven/maven_builder_support"
    ],
    runtime_deps = [
        "//third_party/jvm/org/apache/commons:commons_lang3"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_core",
    exports = [
        "//external:jar/org/apache/maven/maven_core"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/guava:guava",
        "//third_party/jvm/com/google/inject:guice_jar_no_aop",
        "//third_party/jvm/javax/inject:javax_inject",
        "//third_party/jvm/org/apache/commons:commons_lang3",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_api",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_impl",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_spi",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_util",
        "//third_party/jvm/org/apache/maven/shared:maven_shared_utils",
        "//third_party/jvm/org/codehaus/plexus:plexus_classworlds",
        "//third_party/jvm/org/codehaus/plexus:plexus_component_annotations",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils",
        "//third_party/jvm/org/eclipse/sisu:org_eclipse_sisu_inject",
        "//third_party/jvm/org/eclipse/sisu:org_eclipse_sisu_plexus",
        ":maven_artifact",
        ":maven_builder_support",
        ":maven_model",
        ":maven_model_builder",
        ":maven_plugin_api",
        ":maven_repository_metadata",
        ":maven_resolver_provider",
        ":maven_settings",
        ":maven_settings_builder"
    ],
    visibility = [
        "//visibility:public"
    ]
)



java_library(
    name = "maven_model",
    exports = [
        "//external:jar/org/apache/maven/maven_model"
    ],
    runtime_deps = [
        "//third_party/jvm/org/apache/commons:commons_lang3",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_model_builder",
    exports = [
        "//external:jar/org/apache/maven/maven_model_builder"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/guava:guava",
        "//third_party/jvm/org/apache/commons:commons_lang3",
        "//third_party/jvm/org/codehaus/plexus:plexus_component_annotations",
        "//third_party/jvm/org/codehaus/plexus:plexus_interpolation",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils",
        ":maven_artifact",
        ":maven_builder_support",
        ":maven_model"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_plugin_api",
    exports = [
        "//external:jar/org/apache/maven/maven_plugin_api"
    ],
    runtime_deps = [
        "//third_party/jvm/org/codehaus/plexus:plexus_classworlds",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils",
        "//third_party/jvm/org/eclipse/sisu:org_eclipse_sisu_plexus",
        ":maven_artifact",
        ":maven_model"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_repository_metadata",
    exports = [
        "//external:jar/org/apache/maven/maven_repository_metadata"
    ],
    runtime_deps = [
        "//third_party/jvm/org/codehaus/plexus:plexus_utils"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_resolver_provider",
    exports = [
        "//external:jar/org/apache/maven/maven_resolver_provider"
    ],
    runtime_deps = [
        "//third_party/jvm/com/google/guava:guava",
        "//third_party/jvm/javax/inject:javax_inject",
        "//third_party/jvm/org/apache/commons:commons_lang3",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_api",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_impl",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_spi",
        "//third_party/jvm/org/apache/maven/resolver:maven_resolver_util",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils",
        ":maven_model",
        ":maven_model_builder",
        ":maven_repository_metadata"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_settings",
    exports = [
        "//external:jar/org/apache/maven/maven_settings"
    ],
    runtime_deps = [
        "//third_party/jvm/org/codehaus/plexus:plexus_utils"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "maven_settings_builder",
    exports = [
        "//external:jar/org/apache/maven/maven_settings_builder"
    ],
    runtime_deps = [
        "//third_party/jvm/org/apache/commons:commons_lang3",
        "//third_party/jvm/org/codehaus/plexus:plexus_component_annotations",
        "//third_party/jvm/org/codehaus/plexus:plexus_interpolation",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils",
        "//third_party/jvm/org/sonatype/plexus:plexus_sec_dispatcher",
        ":maven_builder_support",
        ":maven_settings"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)


