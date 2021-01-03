# Generator of Unicode regression tests

Suite of tools to create the tests in javatests/de/jflex/testcase/unicode

## Migration

```shell script
versions=( 2.0 2.1 3.0 3.1 3.2 4.0 4.1 5.0 5.1 5.2 6.0 6.1 6.2 6.3 7.0 8.0 9.0 10.0 11.0 12.0 12.1 )
for version in "$versions[@]"; do
  # Generate build file
  bazel run //java/de/jflex/migration/unicodedatatest:generator -- ${version} $(git rev-parse --show-toplevel)
done
```
