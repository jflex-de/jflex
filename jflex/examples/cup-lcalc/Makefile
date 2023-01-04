# Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# make test (or just: make)
#    generates lexer & parser, compiles all *.java files, and runs test
#

# main class
MAIN = Main

# test data
TEST_IN  = src/test/data/test.txt
OUT_GOOD = src/test/data/output.good

# jflex input and output
LEXER_IN = src/main/jflex/lcalc.flex
LEXER_CLASS = Lexer

# cup file
PARSER_IN = src/main/cup/ycalc.cup

include ../common/Makefile.inc
