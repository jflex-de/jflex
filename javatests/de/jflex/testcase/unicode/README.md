<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# End-to-end tests on Unicode data

## Directory layout

```text
javatests/de/jflex/testcase/unicode  All tests related to Unicode support.
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
[java/de/jflex/ucd_test_generator](../../../../../java/de/jflex/ucd_test_generator):

- The test classes and the golden files of the caseless, digit, compatibility
  property, derived core property and emoji tests are generated and checked in.
  Regenerate them with `java/de/jflex/ucd_test_generator/generate.sh`.
- The scanner specs (`*.flex`) of the `unicode_x_y` tests are not checked in. The
  build generates them in `javatests/target/generated-unicode-tests`.
- The golden files of the age tests (`UnicodeAge_x_y_age_*.output`,
  `UnicodeAge_x_y_age_unassigned.output`, `UnicodeAge_x_y_age_subtraction.output`)
  are not generated. They are maintained by hand.
