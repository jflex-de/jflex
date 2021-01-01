# Generator of UnicodeAgeTest_x_y

To create the test for Unicode 6.1

    bazel run //java/de/jflex/migration/unicodedatatest:migrator -- 6.1 "$(git rev-parse --show-toplevel)"

## Migration

```shell script
mv testsuite/testcases/src/test/cases/unicode-age/UnicodeAge_2_1*.output javatests/de/jflex/testcase/unicode/unicode_2_1~
bazel run //java/de/jflex/migration/unicodedatatest:migrator -- 2.1 $(git rev-parse --show-toplevel)
git add javatests/de/jflex/testcase/unicode/unicode_2_1/*
rm testsuite/testcases/src/test/cases/unicode-age/UnicodeAge_2_1*
```
