<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# End-to-end tests on Unicode data

## Directory layout

```text
src/test/java/de/jflex/testcase/unicode  All tests related to Unicode support.
├── unicode_x_y
│   ├── *Test_x_y.java  Tests for version x.y
│   └── *.output        Golden files with the expected ranges
├── *Test.java          Tests unrelated to a specific version.
├── TestingBlock.java   Helper for the block tests.
├── *.flex              Scanner specs of the version-independent tests.
├── *.output            Golden files of the version-independent tests.
└── README.md           This documentation.
```

## Generated content

The content of `unicode_x_y` comes from the generators in
`de.jflex.ucd_test_generator` of the `tests/generators` module:

- The test classes and the golden files are generated and checked in. Regenerate
  them with `scripts/gen-unicode-tests.sh`.
- The scanner specs (`*.flex`) of the `unicode_x_y` tests are not checked in. The
  build generates them in `tests/unicode/target/generated-unicode-tests`.
