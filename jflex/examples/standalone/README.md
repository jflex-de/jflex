JFlex: Standalone Lexer Example
===============================

This is a small example of a lexer with its own main() method that can run on
its own.

## Build, run, test

### Using Maven

To build:

    mvn package

To run the lexer manually:

    java -jar target/standalone-maven-1.0.jar src/test/data/sample.in

To test:

    mvn test


### Using ant

    ant test

will generate, compile, and test the lexer.

### Using make

Use the Makefile to generate the lexer and test it:

    make test

## Files

* `src/main/jflex/standalone.flex`
  The JFlex spec of the standalone scanner.
* `src/test/java/de/jflex/example/standalone/SubstTest.java`
  A test driver to the scanner to compare output.
* `src/test/data/*`
  Sample input with expected output.