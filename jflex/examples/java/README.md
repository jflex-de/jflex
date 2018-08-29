JFlex: Complex example (with CUP)
===============================

This directory contains a scanner and parser for the Java programming language (Java 1.2).

## Prerequisite

You need the parser generator CUP v0.11a for the parser to work.

## Build and run

Use the Makefile or Ant (via 'ant run') to generate the lexer and
parser, or type:

```
jflex unicode.flex
jflex java.flex
java java_cup.Main -interface < java12.cup
javac JavaParser.java TestLexer.java
```

The parser can be tested with:

```
java JavaParser <inputfiles>
```

The scanner (without parser attached) can be test with:

```
java TestLexer <inputfiles>
```

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
