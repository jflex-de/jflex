JFlex: Complex example (with CUP)
===============================

This directory contains a scanner and parser for the Java programming language (Java 1.2).

## Build, run, test

### Using Maven

    ../../mvnw package

To run the parser:

    java -cp target/cup-java-1.0.jar:../../../cup/cup/java-cup-11b-20160615.jar JavaParser <inputfile>

or more simply the uberjar version:

    java -jar target/cup-java-full-1.0.jar <inputfile>


### Using ant

    ant compile
    ant run
    java -jar build:../../../cup/cup/java-cup-11b-20160615.jar JavaParser <inputfiles>

### Using make

Use the Makefile or Ant (via 'ant run') to generate the lexer and
parser, or type:

    make compile
    make test

The parser can be tested with:

    java -cp out:../../../cup/cup/java-cup-11b-20160615.jar JavaParser <inputfiles>

The scanner (without parser attached) can be tested with:

    java -cp out:../../../cup/cup/java-cup-11b-20160615.jar TestLexer <inputfiles>

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
