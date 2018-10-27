SRCS_MAIN = ["Main.java"]

java_library(
    name = "ucd_parser",
    srcs = glob(["*.java"], exclude = SRCS_MAIN),
)

java_binary(
    name = "ucd_parser_bin",
    srcs = SRCS_MAIN,
    deps = [":ucd_parser"],
)
