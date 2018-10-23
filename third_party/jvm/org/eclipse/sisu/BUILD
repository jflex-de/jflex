licenses(["notice"])
java_library(
    name = "org_eclipse_sisu_inject",
    exports = [
        "//external:jar/org/eclipse/sisu/org_eclipse_sisu_inject"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)



java_library(
    name = "org_eclipse_sisu_plexus",
    exports = [
        "//external:jar/org/eclipse/sisu/org_eclipse_sisu_plexus"
    ],
    runtime_deps = [
        "//third_party/jvm/javax/enterprise:cdi_api",
        "//third_party/jvm/org/codehaus/plexus:plexus_classworlds",
        "//third_party/jvm/org/codehaus/plexus:plexus_component_annotations",
        "//third_party/jvm/org/codehaus/plexus:plexus_utils",
        ":org_eclipse_sisu_inject"
    ],
    visibility = [
        "//third_party/jvm:__subpackages__"
    ]
)


