<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# How to add support for a new Unicode version?

## Declare the Unicode.org resources

1. Declare the files fetched from Unicode in `javatests/pom.xml` and
[UcdReleases.java](/java/de/jflex/ucd/UcdReleases.java). See the
[README](/third_party/unicode/README.md).
2. Verify and commit the changes.

## Generate the new UnicodeProperties file

1. Run this (needs JDK 21+):
   ```sh
   scripts/gen-unicode-tables.sh
   ```
2. Verify and commit the changes, including the new `Unicode_x_y.java` in
   [jflex/core/unicode/data](/jflex/src/main/java/jflex/core/unicode/data).

## Add the Unicode test case

Add a test case in [javatests/de/jflex/testcase/unicode](/javatests/de/jflex/testcase/unicode).

1. Extend `KNOWN_VERSIONS` in
   [AbstractGenerator.java](/java/de/jflex/ucd_test_generator/base/AbstractGenerator.java).
2. Run the script which runs all the generators:
   ```sh
   java/de/jflex/ucd_test_generator/generate.sh
   ```
3. Verify and commit the changes.

The Unicode cases of the test suite in [testsuite/testcases](/testsuite/testcases) are
generated from the UCD for every version in `UcdReleases`, so they need no change. The
implementations of the UAX #14 and UAX #29 segmentation rules in
[testsuite/testcases/src/test/uax](/testsuite/testcases/src/test/uax) are hand-written: copy
the spec and `.test` file of the previous version under the new version's name, and update
the rules.
