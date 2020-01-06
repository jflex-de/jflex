JFlex: Java 1.2 Parser (with CUP)
=================================

This directory contains a scanner and parser for the Java programming language (Java 1.2).

## Build, run, test

### Using Maven

To build:

    mvn package

To run the parser manually:

    java -jar target/cup-java-full-1.0.jar <inputfile>

To test the lexer:

    mvn test


### Using ant

    ant test

will generate, compile, and test the parser and lexer.

### Using make

Use the Makefile to generate the lexer and parser, and test it:

    make test

## Files

* `unicode.flex`
  JFlex specification for the Unicode preprocessing phase
  (see section 3.3 of the Java Language Specification).
  Demonstrates how to implement a FilterReader with JFlex.
* `java.flex`
  JFlex specification for the "real" Java 1.2 lexer.
* `java12.cup`
  CUP specification for the Java 1.2 parser
  Copyright (C) 1998 by C. Scott Ananian <cananian@alumni.princeton.edu>
  (with small modifications to interface with the Lexer)
* `JavaParser.java`
  a simple main class to run the parser (no other useful output though)
* `TestLexer.java`
  a simple test driver for the scanner, produces debug output
* `lexer-output.good`
  Golden output file corresponding to `TestLexer.java`.
