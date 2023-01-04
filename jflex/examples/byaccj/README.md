<!--
  Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

JFlex with Yacc
===============

This directory contains an interoperability example for [BYacc/J][byaccj] and
JFlex. The example implements a small calculator.

## Build, run, test

### Build with Make

You need [BYacc/J][byaccj] installed on your system.

Use the Makefile to generate the lexer and parser.

```
make all

```

The example can then be started with

```
make run
```

## Files

* `src/main/jflex/calc.flex`
  JFlex specification for the lexical part of the arithmetic expressions.

* `src/main/yacc/calc.y`
  BYacc/J specification and main program for the calculator.


[byaccj]: http://byaccj.sourceforge.net/