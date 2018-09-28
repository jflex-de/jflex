
JFLEX      = ../../bin/jflex


LEXER      = ZeroLexer

# --------------------------------------------------

all: test

test: out/output.txt
	@(diff --strip-trailing-cr src/test/data/lexer-output.good $<  && echo "Test OK!")

out/output.txt: out/$(LEXER).class src/test/data/test-input.txt
	java -cp out $(LEXER) src/test/data/test-input.txt > $@

compile: out/$(LEXER).class
	
out/$(LEXER).class: out/$(LEXER).java src/main/java/*.java
	javac -d out $^

out/$(LEXER).java: src/main/jflex/zero-lexer.jflex
	$(JFLEX) -d out $<

clean:
	rm -rf out
