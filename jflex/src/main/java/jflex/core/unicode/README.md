<!--
  Copyright 2021, Régis Décamps
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Unicode properties

These files have been generated automatically.
Eventually, they should be removed from SCM.

## Generate from Bazel

```sh
bazel build //java/de/jflex/ucd_generator:gen_unicode_properties
google-java-format bazel-bin/java/de/jflex/ucd_generator/UnicodeProperties.java > jflex/src/main/java/jflex/core/unicode/UnicodeProperties.java
for f in bazel-bin/java/de/jflex/ucd_generator/Unicode_*.java; do
  google-java-format $f  > jflex/src/main/java/jflex/core/unicode/data/$(basename $f)
done
```

See also

* [/third_party/unicode](/third_party/unicode) for how to add a new version from Unicode.org
* [ucd_generator](/java/jflex/ucd_generator) for the implementation details.
