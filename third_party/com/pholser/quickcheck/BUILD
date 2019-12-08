# junit-quickcheck
# junit-quickcheck is a library that supports writing and running property-based tests in JUnit,
# inspired by QuickCheck for Haskell.
# https://github.com/pholser/junit-quickcheck

package(default_visibility = ["//visibility:public"])

licenses(["notice"])  # MIT License

java_library(
    name = "quickcheck",
    testonly = True,
    exports = [
        "@maven//:com_pholser_junit_quickcheck_core",
        "@maven//:com_pholser_junit_quickcheck_generators",
    ],
)
