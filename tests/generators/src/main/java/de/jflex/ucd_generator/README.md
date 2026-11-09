<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# UCD Generator

This program parses the fileset of a Unicode Character Definition,
and generates

* a `UnicodeProperties_X_Y` java class per version
* one `UnivodeProperties.java` for all version

## Where are the Unicode.org resources defined

The downloads are in [tests/unicode/pom.xml](/tests/unicode/pom.xml), and
[UcdReleases](../ucd/UcdReleases.java) maps each Unicode version to its downloads.
See [the how-to](/docs/howto/new_unicode_version.md) for adding a version.

## How to generate the Java files

    scripts/gen-unicode-tables.sh

This needs JDK 21+. It downloads the files, runs `AllVersionsMain` through the
Maven profile `gen-unicode-tables` of `tests/unicode`, and copies and formats the
result into [jflex/core/unicode](/jflex/src/main/java/jflex/core/unicode).

### Building for a reduced set of properties

The UnicodeData.txt and DerivedAge.txt must be provided.
The other data files should be provided.

    java -cp <classpath> de.jflex.ucd_generator.Main --version=5.0.0 $(ls /tmp/ucd_5_0/*.txt /tmp/ucd_5_0/auxiliary/*.txt) --out=/tmp/ucd5_java

## Software architecture.

* `Main` is only responsible for parsing the cli arguments
  and generating `UcdVersions`
* The model `UcdVersions` is a map of version → File for each type, and is light in memory.
  * The types are defined in a simple enum `UcdFileType`.
* The different Unicode.org files are parsed by `UcdScanner`.
  * using JFlex (the flex files have been copied with little modifications form the jflex-unicode-maven-plugin)
  * The business logic of the scanners is in an class, so that the class can easily be edited in IDE
* While parsing, the `UnicodeData` model is updated.
  * I've tried to use immutable objects when possible, but that wasn't always possible
* `UcdGenerator` fills in templates with `de.jflex.template`
  * `UnicodePropertiesEmitter` emits `UnicodeProperties.java` from the `UcdVersions`.
  * `UnicodeVersionEmitter` emits `Unicode_x_y.java` from the `UnicodeData`.
