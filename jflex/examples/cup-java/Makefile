# Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# make test (or just: make)
#    generates lexer & parser, compiles all *.java files, and runs test
#

# main class
MAIN = TestLexer

# test data
TEST_IN  = src/main/java/TestLexer.java
OUT_GOOD = src/test/data/lexer-output.good

# jflex input and output
LEXER_IN = src/main/jflex/java.flex
LEXER_CLASS = Scanner

# cup file
PARSER_IN = src/main/cup/java12.cup

# this example has additional generated java files
MORE_JAVA_FILES = $(GEN)/UnicodeEscapes.java

include ../common/Makefile.inc

# second lexer, target after includes, so "all" remains default target
$(MORE_JAVA_FILES): src/main/jflex/unicode.flex
	$(JFLEX) -d $(GEN) $<

# this example has a second test, on the parser level, instead of lexer
run-parser:
	java -cp $(CUPJAR):$(OUT) JavaParser $(TEST_IN)

all: test run-parser