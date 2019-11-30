# JFlex test suite

## Run the test suite

To run all regression tests:

    bazel test //javatests/jflex/testcase/...
    
## Migration from Maven to Bazel

There are only a few tests, as their migration from Bazel is work in progress.

See [Migration instructions](https://github.com/jflex-de/jflex/wiki/Migration-to-Bazel#migrate-a-golden-test).

### Not migrated

- **encoding**.
  Bazel makes it hard to
  [change the encoding used by javac](https://stackoverflow.com/a/43472003/94363).
  It's UTF-8 by default.
  And the use of `--encoding` changes both the input `.flex` and the generated `.java`
  files.
