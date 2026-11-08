<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# JFlex test suite

This directory contains a conformance test suite for JFlex, mostly for its Unicode
properties. The cases are run by `de.jflex.testsuite.TestsuiteTest` in the `javatests`
module, which defines the format of the `.test` files in `TestLoader.flex`.

## Layout

- `testcases/src/test/cases/<dir>/<name>.test`: one test case. It names the spec
  `<name>.flex`, the JFlex options, and the scanner's input.
- `<name>-flex.output`: the expected JFlex messages. Paths in it are relative to
  `testcases`.
- `<name>.output`, or `<name>-*.input` and `<name>-*.output`: the scanner's input and
  expected output.
- `testcases/src/test/resources`: input files and Java code shared by the specs.
- `testcases/src/test/{perl,sh,java}`: the scripts that created the Unicode cases.

## Running

With `jflex` and `jflex-maven-plugin` installed, from the root directory:

    ./mvnw -pl java,javatests install -Dtest=TestsuiteTest -Dsurefire.failIfNoSpecifiedTests=false

`scripts/test-regression.sh` runs the same command. Add `-Dtestcases=dot,unicode-scripts` to
run only some case directories.
