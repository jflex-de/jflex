# Generator of Unicode regression tests

Suite of tools to create the tests in javatests/de/jflex/testcase/unicode

## Migration

```shell script
bazel build java/de/jflex/migration/unicodedatatest:generate
cp -rf bazel-bin/java/de/jflex/migration/unicodedatatest/javatests .
```
