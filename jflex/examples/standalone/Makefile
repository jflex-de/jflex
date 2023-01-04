# Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# make test (or just: make)
#    generates lexer & parser, compiles all *.java files, and runs test
#

# main class
MAIN = de.jflex.example.standalone.Subst

# test data
TEST_IN  = src/test/data/sample.in
OUT_GOOD = src/test/data/sample.expected

# jflex input
LEXER_IN = src/main/jflex/standalone.flex
LEXER_CLASS = Subst

# no cup file in this example
PARSER_OUT =

# no other java files in this example
MAIN_JAVA_FILES =

include ../common/Makefile.inc
