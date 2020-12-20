# Unicode properties

These files have been generated automatically.
Eventually, they should be removed from SCM.

## Generate from Bazel

```sh
bazel build //jflex/src/main/java/jflex/core/unicode:gen_unicode_properties
cp bazel-bin/jflex/src/main/java/jflex/core/unicode/UnicodeProperties.java jflex/src/main/java/jflex/core/unicode 
cp bazel-bin/jflex/src/main/java/jflex/core/unicode/Unicode_*.java jflex/src/main/java/jflex/core/unicode/data

```

## Generate from jflex-unicode-maven-plugin

TODO: Document