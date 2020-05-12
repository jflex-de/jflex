# This file is included in all example Makefiles.
# It defines targets and variables common to all examples.

# Paths are relative to the example project directories

JFLEX      = ../../bin/jflex

LIB        = ../common/lib
CURL       = curl -L -O -C -

CUPVERSION = 11b-20160615
CUPJAR     = java-cup-$(CUPVERSION).jar
CUPURL     = https://repo1.maven.org/maven2/com/github/vbmacher/java-cup/$(CUPVERSION)/$(CUPJAR)
CUP        = java -jar $(LIB)/$(CUPJAR)

JSR250VERSION = 1.0
JSR250JAR     = jsr250-api-$(JSR250VERSION).jar
JSR250URL     = https://repo1.maven.org/maven2/javax/annotation/jsr250-api/$(JSR250VERSION)/$(JSR250JAR)

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

$(OUT)/$(MAIN).class: $(LEXER) $(PARSER) $(MAIN_JAVA_FILES) $(MORE_JAVA_FILES) $(LIB)/$(CUPJAR) $(LIB)/$(JSR250JAR)
	javac -cp $(LIB)/$(CUPJAR):$(LIB)/$(JSR250JAR):$(OUT) -d $(OUT) $(MAIN_JAVA_FILES) $(GEN)/*.java

gen_jflex: $(LEXER)

$(LEXER): $(LEXER_IN)
	$(JFLEX) -d $(GEN) $<

gen_cup: $(PARSER)

$(PARSER): $(PARSER_IN) $(LIB)/$(CUPJAR)
	$(CUP) -interface < $<
	mkdir -p $(GEN)
	mv $(PARSER_OUT) $(GEN)/

$(LIB)/$(CUPJAR):
	mkdir -p $(LIB)
	(cd $(LIB); $(CURL) $(CUPURL))

$(LIB)/$(JSR250JAR):
	mkdir -p $(LIB)
	(cd $(LIB); $(CURL) $(JSR250URL))

run: compile $(TEST_IN)
	java -cp $(LIB)/$(CUPJAR):$(LIB)/$(JSR250JAR):$(OUT) $(MAIN) $(PIPE_INPUT) $(TEST_IN) > $(OUT_ACTUAL)
	@cat $(OUT_ACTUAL)

test: run
	@(diff $(OUT_ACTUAL) $(OUT_GOOD) && echo "Test OK!") || (echo "Test FAILED"; exit 1)

clean:
	rm -rf $(OUT)
