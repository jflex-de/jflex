# JFlex test suite

## Run the test suite

To run all regression tests:

    bazel test //javatests/de/jflex/testcase/...
    
## Migration from Maven to Bazel

There are only a few tests, as their migration from Bazel is work in progress.

See [Migration instructions](https://github.com/jflex-de/jflex/wiki/Migration-to-Bazel#migrate-a-golden-test).

## Writing a new test

Note: It is important than any jflex rule depends on the current JFlex version
so that we don't test the bootstrap version.

    jflex_bin = "//jflex:jflex_bin",
