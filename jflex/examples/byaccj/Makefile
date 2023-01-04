# Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
# SPDX-License-Identifier: BSD-3-Clause

# Be sure to use yacc/j ; not unix yacc nor bison.
BYACCJ = yaccj -J
JFLEX  = ../../bin/jflex

run: all
	java -cp out Parser

all: build

build: Parser.class

clean:
	rm -rf out

Parser.class: Yylex.java Parser.java
	javac -d out out/*.java

Yylex.java: src/main/jflex/calc.flex
	$(JFLEX) -d out $<

Parser.java: src/main/yacc/calc.y
	$(BYACCJ) $< ;  mv Parser.java ParserVal.java out
