# Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# make test (or just: make)
#    generates lexer & parser, compiles all *.java files, and runs test
#

# main class
MAIN = Yylex

# test data
TEST_IN  = src/test/data/test.txt
OUT_GOOD = src/test/data/output.good

# jflex input
LEXER_IN = src/main/jflex/simple.flex

# no cup file in this example
PARSER_OUT =

include ../common/Makefile.inc
