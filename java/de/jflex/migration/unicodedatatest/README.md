# Generator of UnicodeAgeTest_x_y

To create the test for Unicode 6.1

    bazel run //java/de/jflex/migration/unicodedatatest:migrator -- 6.1 "$(git rev-parse --show-toplevel)"

## Migration

```shell script
version="2.1"
v=${version/\./_}
bazel run //java/de/jflex/migration/unicodedatatest:migrator -- ${version} $(git rev-parse --show-toplevel)
git rm testsuite/testcases/src/test/cases/unicode-age/UnicodeAge_${v}/*-flex.output
git mv testsuite/testcases/src/test/cases/unicode-age/UnicodeAge_${v}*.output javatests/de/jflex/testcase/unicode/unicode_${v}
git add javatests/de/jflex/testcase/unicode/unicode_${v}/*
git rm testsuite/testcases/src/test/cases/unicode-age/UnicodeAge_${v}*
```
