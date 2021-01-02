# Unicode blocks tests

```sh
bazel run java/de/jflex/migration/unicodedatatest/testblock:generator -- 6.1 $(git rev-parse --show-toplevel) /path/to/Blocks.txt
```

Since we have already downloaded the Unicode data, this genrule will produce 
everything:

```
for v in ( 2_0 2_1 3_0 3_1 3_2 4_0 4_1 5_0 5_1 5_2 6_0 6_1 6_2 6_3 7_0 8_0 9_0 10_0 11_0 12_0 12_1 ); do
  git mv testsuite/testcases/src/test/cases/unicode-blocks/UnicodeBlocks_${v}.flex javatests/de/jflex/testcase/unicode/unicode_${v}
done
bazel build java/de/jflex/migration/unicodedatatest/testblock:generate
cp -r bazel-bin/java/de/jflex/migration/unicodedatatest/testblock/javatests .
```
