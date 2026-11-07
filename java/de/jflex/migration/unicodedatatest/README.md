<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Generator of Unicode regression tests

Suite of tools to create the tests in javatests/de/jflex/testcase/unicode

## Generate all tests

1. Extend `KNOWN_VERSIONS` in [build_defs.bzl](build_defs.bzl) and
   [base/AbstractGenerator.java](base/AbstractGenerator.java), and add the UCD releases of the new version
   to `releases` in [AllTestsGenerator.java](AllTestsGenerator.java).
2. Run the script, which needs `google-java-format` and `buildifier`
   ```shell script
   java/de/jflex/migration/unicodedatatest/generate.sh
   ```
