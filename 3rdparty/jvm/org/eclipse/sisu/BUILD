java_library(
    name = "org_eclipse_sisu_inject",
    exports = [
        "//external:jar/org/eclipse/sisu/org_eclipse_sisu_inject"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)



java_library(
    name = "org_eclipse_sisu_plexus",
    exports = [
        "//external:jar/org/eclipse/sisu/org_eclipse_sisu_plexus"
    ],
    runtime_deps = [
        "//3rdparty/jvm/javax/enterprise:cdi_api",
        "//3rdparty/jvm/org/codehaus/plexus:plexus_classworlds",
        "//3rdparty/jvm/org/codehaus/plexus:plexus_component_annotations",
        "//3rdparty/jvm/org/codehaus/plexus:plexus_utils",
        ":org_eclipse_sisu_inject"
    ],
    visibility = [
        "//3rdparty/jvm:__subpackages__"
    ]
)


