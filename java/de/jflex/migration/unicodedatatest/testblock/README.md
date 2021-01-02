# Unicode blocks tests

```sh
bazel run java/de/jflex/migration/unicodedatatest/testblock:generator -- 6.1 $(git rev-parse --show-toplevel) /path/to/Blocks.txt
```

Since we have already downloaded the Unicode data, this genrule will produce 
everything:

```
git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_2_0.flex javatests/de/jflex/testcase/unicode/unicode_2_0/UnicodeBlocks_2_0.flex
bazel build java/de/jflex/migration/unicodedatatest/testblock:generate
cp bazel-bin/java/de/jflex/migration/unicodedatatest/testblock/javatests/de/jflex/testcase/unicode/unicode_2_0/UnicodeBlocks_2_0.flex javatests/de/jflex/testcase/unicode/unicode_2_0/UnicodeBlocks_2_0.flex
```
