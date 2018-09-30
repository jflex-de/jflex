Simple usage of the jflex-maven-plugin
======================================

This is a small Maven project that demonstrates the use of `jflex-maven-plugin`.

## Build with maven

    ../../mvnw package site


## Files

* `main/flex/simple.flex`:
  the example specification
* `test/resources/test.txt`:
  sample input
* `test/resources/output.good`:
  expected output matching the sample input from `test.txt`

## Artfifacts

* `target/generated-sources/jflex` Java code generated from the flex file.
