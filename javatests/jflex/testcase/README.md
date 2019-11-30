# JFlex test suite

## Run the test suite

To run all regression tests:

    bazel test //javatests/jflex/testcase/...
    
## Migration from Maven to Bazel

There are only a few tests, as their migration from Bazel is work in progress.

See [Migration instructions](https://github.com/jflex-de/jflex/wiki/Migration-to-Bazel#migrate-a-golden-test).

### Not migrated

- **ccl_pre** with JDK variants.
  ccl-pre/ccl2.test was not migrated.
  Bazel makes it hard to change the runtime.
