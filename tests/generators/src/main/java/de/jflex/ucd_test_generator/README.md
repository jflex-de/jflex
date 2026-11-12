<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Generator of Unicode regression tests

Suite of tools to create the tests in `tests/unicode/src/test/java/de/jflex/testcase/unicode`

## Generate all tests

1. For a new Unicode version, add the version and its UCD releases to
   [UcdReleases.java](../../ucd/UcdReleases.java). The tests are generated for every version in
   it.

2. Run the script, which needs `google-java-format`

   ```sh
   scripts/gen-unicode-tests.sh
   ```
