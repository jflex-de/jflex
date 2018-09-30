Simple usage of the jflex-maven-plugin
======================================

## Build with maven

    ../../mvnw package site

## Build with Bazel

    blaze build //simple

The generated lexer will be in (`examples/`)`bazel-genfiles/simple/Yylex.java`.


## Files

* `main/flex/simple.flex`:
  the example specification
* `test/resources/test.txt`:
  sample input
* `test/resources/output.good`:
  expected output matching the sample input from `test.txt`

## Artfifacts

* `target/generated-sources/jflex` Java code generated from the flex file.
