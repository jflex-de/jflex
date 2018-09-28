JFlex with Yacc
===============

This directory contains an interoperability example for [BYacc/J][byaccj] and JFlex.
This example implements a small calculator.

## Build and run

Use the Makefile to generate the lexer and parser.

```
make all

```

The example can then be started with

```
make run
```

## Files

* `calc.flex`
  JFlex specification for the lexical part of the arithmetic expressions.

* `calc.y`
  BYacc/J specification and main program for the calculator.


[byaccj]: http://byaccj.sourceforge.net/