# Unicode properties

These files have been generated automatically.
Eventually, they should be removed from SCM.

## Generate from Bazel

```sh
bazel build //java/de/jflex/ucd_generator:gen_unicode_properties
google-java-format bazel-bin/jflex/src/main/java/jflex/core/unicode/UnicodeProperties.java > jflex/src/main/java/jflex/core/unicode
for f in bazel-bin/jflex/src/main/java/jflex/core/unicode/Unicode_*.java; do
  google-java-formar bazel-bin/jflex/src/main/java/jflex/core/unicode/Unicode_*.java > jflex/src/main/java/jflex/core/unicode/data
done
```

## Generate from jflex-unicode-maven-plugin

TODO: Document
