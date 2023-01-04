# Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# This file is included in all example Makefiles.
# It defines targets and variables common to all examples.

# Paths are relative to the example project directories

JFLEX      = ../../bin/jflex

LIB        = ../common/lib
CUPVERSION = java-cup-11b-20160615.jar
CUPURL     = https://raw.githubusercontent.com/jflex-de/jflex/7a3d66f2a/cup/cup
CUPJAR     = $(LIB)/$(CUPVERSION)
CUP        = java -jar $(CUPJAR)

OUT    = out
GEN    = $(OUT)/genfiles
MAIN_JAVA_FILES ?= src/main/java/*.java
MORE_JAVA_FILES ?=

LEXER_CLASS ?= Yylex
LEXER  = $(GEN)/$(LEXER_CLASS).java

PARSER_OUT ?= parser.java sym.java
PARSER ?= $(addprefix $(GEN)/,$(PARSER_OUT))

OUT_ACTUAL = $(OUT)/output.txt


all: test

compile: $(OUT)/$(MAIN).class

$(OUT)/$(MAIN).class: $(LEXER) $(PARSER) $(MAIN_JAVA_FILES) $(MORE_JAVA_FILES)
	javac -cp $(CUPJAR):$(OUT) -d $(OUT) $(MAIN_JAVA_FILES) $(GEN)/*.java

gen_jflex: $(LEXER)

$(LEXER): $(LEXER_IN)
	$(JFLEX) -d $(GEN) $<

gen_cup: $(PARSER)

$(PARSER): $(PARSER_IN) $(CUPJAR)
	$(CUP) -interface < $<
	mkdir -p $(GEN)
	mv $(PARSER_OUT) $(GEN)/

$(CUPJAR):
	mkdir -p $(LIB)
	(cd $(LIB); curl -O $(CUPURL)/$(CUPVERSION))

run: compile $(TEST_IN)
	java -cp $(CUPJAR):$(OUT) $(MAIN) $(PIPE_INPUT) $(TEST_IN) > $(OUT_ACTUAL)
	@cat $(OUT_ACTUAL)

test: run
	@(diff $(OUT_ACTUAL) $(OUT_GOOD) && echo "Test OK!") || (echo "Test FAILED"; exit 1)

clean:
	rm -rf $(OUT)
