<!--
  Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# JFlex with cup

This directory contains a small example of integration between JFlex and
[CUP][cup].

It comes from a [short article series in the Linux Gazette][1] by Richard A.
Sevenich and Christopher Lopes, titled _Compiler Construction Tools_.

Small changes and updates to newest JFlex+Cup versions by Gerwin Klein.

## Build, run, test

### Using Maven

To compile:

    mvn package

To run:

    java -jar target/cup-lcalc-full-1.0.jar src/test/data/test.txt

To test automatically:

    mvn test

This will compile and test that the `src/test/data/test.txt` file
is parsed and generates the expected `src/test/data/output.good`.

### Using Ant

    ant test

will build and test the calculator example.

### Using Make

    make test

will build and test the calculator example.

## Files

* `src/main/java/Main.java`         demo of a main program
* `src/main/flex/lcalc.flex`        the lexer spec
* `src/main/cup/ycalc.cup`          the parser spec
* `src/test/data/test.txt`     sample input for testing
* `src/test/data/output.good`  how the output should look like for the test
* `pom.xml`                         the Maven Project model
  - The `jflex-maven-plugin` generates `Lexer.java` from `lcalc.flex`
  - The `cup-maven-plugin` generates `sym.class` from `ycalc.cup`

[cup]: http://www2.cs.tum.edu/projects/cup/
[1]: http://www.linuxgazette.com/issue39/sevenich.html
