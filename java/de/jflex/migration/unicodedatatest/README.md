<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Generator of Unicode regression tests

Suite of tools to create the tests in javatests/de/jflex/testcase/unicode

## Generate all tests

1. Extend `KNOWN_VERSIONS` in [unicodedatatest/build_defs.bzl](unicodedatatest/build_defs.bzl)
2. Run the script    
   ```shell script
   java/de/jflex/migration/unicodedatatest/generate.sh
   ```
