<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Unicode Character Definitions

The Unicode character definitions (UCD) from unicode.org are downloaded at build
time, by the `download-maven-plugin` executions in
[javatests/pom.xml](/javatests/pom.xml). They end up in `javatests/target/ucd` and
in the cache in `~/.m2/repository/.cache/download-maven-plugin`.

For further information about data files please see:

Unicode Character Database
	http://www.unicode.org/ucd/

Terms of Use
	http://www.unicode.org/copyright.html

## How to add Unicode data (UCD)?

### Add the downloads

Add an execution of `download-maven-plugin` in [javatests/pom.xml](/javatests/pom.xml).
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
[UcdReleases.java](/java/de/jflex/ucd/UcdReleases.java), for instance:

```java
add("9.0.0", "ucd_9_0", "emoji_4");
```

The files that the generators use from these directories, such as
`auxiliary/WordBreakProperty.txt` in the zip or `ScriptExtensions.txt` since Unicode
6.0, are found by name.

See also [How to add support for a new Unicode version?](/docs/howto/new_unicode_version.md)
