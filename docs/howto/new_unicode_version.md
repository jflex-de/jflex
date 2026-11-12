<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# How to add support for a new Unicode version?

The Unicode Character Database (UCD) from unicode.org is downloaded at build time, by the
`download-maven-plugin` executions in [tests/unicode/pom.xml](/tests/unicode/pom.xml). The files
end up in `tests/unicode/target/ucd` and in the cache in
`~/.m2/repository/.cache/download-maven-plugin`.

## Declare the Unicode.org resources

### Add the downloads

Add an execution of `download-maven-plugin` in [tests/unicode/pom.xml](/tests/unicode/pom.xml).
Its output directory is the name of the release. For instance:

```xml
<execution>
  <id>ucd_9_0</id>
  <goals><goal>wget</goal></goals>
  <configuration>
    <url>${ucd.url}/zipped/9.0.0/UCD.zip</url>
    <outputDirectory>${ucd.dir}/ucd_9_0</outputDirectory>
    <sha256>df9e028425816fd5117eaea7173704056f88f7cd030681e457c6f3827f9390ec</sha256>
    <unpack>true</unpack>
  </configuration>
</execution>
```

* The sha256 is the sha of the zip file.
  Tip: Use a fake value and let the build fail. It reports the actual value.
* Unicode.org offers a zip since Unicode 4.1. For older versions, each file has its
  own execution.
* The emoji data of Unicode 8.0 and later is not in the zip. It needs an execution of
  its own, with the URL `${ucd.url}/emoji/x.y/emoji-data.txt`, unless the matching
  emoji version is already there.

### Add the version

Add the version and its output directories to `RELEASES` in
[UcdReleases.java](/tests/generators/src/main/java/de/jflex/ucd/UcdReleases.java), for instance:

```java
add("9.0.0", "ucd_9_0", "emoji_4");
```

The files that the generators use from these directories, such as
`auxiliary/WordBreakProperty.txt` in the zip or `ScriptExtensions.txt` since Unicode
6.0, are found by name.

Verify and commit the changes.

## Generate the new UnicodeProperties file

1. Run this (needs JDK 21+):
   ```sh
   scripts/gen-unicode-tables.sh
   ```
2. Verify and commit the changes, including the new `Unicode_x_y.java` in
   [jflex/core/unicode/data](/jflex/src/main/java/jflex/core/unicode/data).

## Add the Unicode test case

Add a test case in
[tests/unicode/src/test/java/de/jflex/testcase/unicode](/tests/unicode/src/test/java/de/jflex/testcase/unicode).

1. Run the script which runs all the generators:

   ```sh
   scripts/gen-unicode-tests.sh
   ```

2. Verify and commit the changes.

The Unicode `.test` cases of [tests/unicode](/tests/unicode) are generated from the UCD for
every version in `UcdReleases`, so they need no change. The implementations of the UAX #14 and
UAX #29 segmentation rules in
[tests/unicode/src/test/uax](/tests/unicode/src/test/uax) are hand-written: copy
the spec and `.test` file of the previous version under the new version's name, and update
the rules.
