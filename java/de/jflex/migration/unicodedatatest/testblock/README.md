# Unicode blocks tests

```sh
bazel run java/de/jflex/migration/unicodedatatest/testblock:generator -- 6.1 $(git rev-parse --show-toplevel) /path/to/Blocks.txt
```

Since we have already downloaded the Unicode data, this genrule will produce 
everything:

```
git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_2_0.flex javatests/de/jflex/testcase/unicode/unicode_2_0
git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_2_1.flex javatests/de/jflex/testcase/unicode/unicode_2_1
git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_3_0.flex javatests/de/jflex/testcase/unicode/unicode_3_0
git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_3_1.flex javatests/de/jflex/testcase/unicode/unicode_3_1
git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_3_2.flex javatests/de/jflex/testcase/unicode/unicode_3_2
git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_4_0.flex javatests/de/jflex/testcase/unicode/unicode_4_0
bazel build java/de/jflex/migration/unicodedatatest/testblock:generate
cp -r bazel-bin/java/de/jflex/migration/unicodedatatest/testblock/javatests .
```
