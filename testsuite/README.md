<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# JFlex test suite

This directory contains a conformance test suite for JFlex, mostly for its Unicode
properties. The cases are run by `de.jflex.testsuite.TestsuiteTest` in the `javatests`
module, which defines the format of the `.test` files in `TestLoader.flex`.

## Layout

- `testcases/src/test/cases/<dir>/<name>.test`: one hand-written test case. It names the spec
  `<name>.flex`, the JFlex options, and the scanner's input.
- `<name>-flex.output`: the expected JFlex messages. Paths in it are relative to
  `testcases`.
- `<name>.output`, or `<name>-*.input` and `<name>-*.output`: the scanner's input and
  expected output.
- `testcases/src/test/resources`: input files and Java code shared by the specs.
- `testcases/src/test/uax/<dir>/`: the specs and `.test` files of the hand-written
  implementations of the UAX #14 and UAX #29 segmentation rules, one per Unicode version.
  Their inputs are the `*BreakTest.txt` files of the UCD, and the goldens are the expected
  breaks in those files.
- `testcases/src/test/java`: the program that creates the input files in `resources`.

## Generated Unicode test cases

The test cases for Unicode properties (general category, scripts, line break, ...) are not checked
in. `de.jflex.testsuite.generator.UnicodeCasesGenerator` in the `java` module generates them from
the Unicode Character Database for every version in `de.jflex.ucd.UcdReleases` when the `javatests`
module builds. They are saved into `javatests/target/generated-testsuite`, which has the same layout
as `testcases`. The UAX cases are assembled there from `src/test/uax` and the UCD.

The test case generation is separate from the JFlex implementation.

## Running

With `jflex` and `jflex-maven-plugin` installed, from the root directory:

    ./mvnw -pl java,javatests install -Dtest=TestsuiteTest -Dsurefire.failIfNoSpecifiedTests=false

`scripts/test-regression.sh` runs the same command. Add `-Dtestcases=dot,unicode-scripts` to
run only some case directories.
