<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Generator of Unicode regression tests

Suite of tools to create the tests in `tests/unicode/src/test/java/de/jflex/testcase/unicode`

## Generate all tests

1. Extend `KNOWN_VERSIONS` in [base/AbstractGenerator.java](base/AbstractGenerator.java). The UCD
   releases of the new version go into [UcdReleases.java](../../ucd/UcdReleases.java).
2. Run the script, which needs `google-java-format`
   ```shell script
   scripts/gen-unicode-tests.sh
   ```
