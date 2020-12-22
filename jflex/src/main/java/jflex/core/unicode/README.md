# Unicode properties

These files have been generated automatically.
Eventually, they should be removed from SCM.

## Generate from Bazel

```sh
bazel build //jflex/src/main/java/jflex/core/unicode:gen_unicode_properties
cp bazel-bin/jflex/src/main/java/jflex/core/unicode/UnicodeProperties.java jflex/src/main/java/jflex/core/unicode 
cp bazel-bin/jflex/src/main/java/jflex/core/unicode/Unicode_*.java jflex/src/main/java/jflex/core/unicode/data

```

See also
* [/third_party/unicode](/third_party/unicode) for how to add a new version from Unicode.org 
* [ucd_generator](/java/jflex/ucd_generator) for the impelentation details. 

## Generate from jflex-unicode-maven-plugin

```sh
./mvnw install
cd jflex
../mvnw generate-sources -P generate-unicode-properties
```
