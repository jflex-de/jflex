# You need CUP v0.10j (or newer) for this makefile to work (for java12.cup)
#
# CUP classes should be included in CLASSPATH 

ROOT       = ../../..
JFLEX      = $(ROOT)/jflex/bin/jflex
CUPJAR     = $(ROOT)/cup/cup/java-cup-11b.jar
CUP        = java -jar $(CUPJAR)


all: clean compile

compile: gen_all src/main/java/*.java
	javac -d out -cp $(CUPJAR) out/genfiles/*.java src/main/java/*.java

test: out/lexer-output.txt
	@(diff $< src/test/data/lexer-output.good && echo "Test OK") || (echo "Test FAILED"; exit 1)

out/lexer-output.txt: compile outdir
	java -cp out:$(CUPJAR) TestLexer src/main/java/TestLexer.java > out/lexer-output.txt

gen_all: gen_parser gen_scanner gen_unicode

gen_parser: src/main/cup/java12.cup outdir
	$(CUP) -interface $<
	mv parser.java sym.java out/genfiles

gen_scanner: src/main/jflex/java.flex outdir
	$(JFLEX) -d out/genfiles $<

gen_unicode: src/main/jflex/unicode.flex outdir
	$(JFLEX) -d out/genfiles $<

clean:
	rm -rf out

outdir:
	mkdir -p out/genfiles
