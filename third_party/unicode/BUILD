package(default_visibility = ["//visibility:public"])

licenses(["notice"])

exports_files(["LICENSE"])

filegroup(
    name = "ucd_1",
    srcs = [
        "@ucd_1_UnicodeData_1_1_5_txt//file",
    ],
)

filegroup(
    name = "ucd_2",
    srcs = [
        "@ucd_2_Blocks_2_txt//file",
        "@ucd_2_PropList_2_1_9_txt//file",
        "@ucd_2_UnicodeData_2_1_9_txt//file",
    ],
)

filegroup(
    name = "ucd_3",
    srcs = [
        "@ucd_3_Blocks_3_2_0_txt//file",
        "@ucd_3_DerivedCoreProperties_3_2_0_txt//file",
        "@ucd_3_LineBreak_3_2_0_txt//file",
        "@ucd_3_PropList_3_2_0_txt//file",
        "@ucd_3_PropertyAliases_3_2_0_txt//file",
        "@ucd_3_PropertyValueAliases_3_2_0_txt//file",
        "@ucd_3_Scripts_3_2_0_txt//file",
        "@ucd_3_UnicodeData_3_2_0_txt//file",
    ],
)

alias(
    name = "ucd_4",
    actual = "@ucd_4//:files",
)
