
JFLEX      = ../../bin/jflex
JAVA       = java
JAVAC      = javac

LEXER      = ZeroLexer

# --------------------------------------------------

all: test

test: output.txt
	@(diff output.txt lexer-output.good && echo "Test OK!") || echo "Test failed!"

output.txt: $(LEXER).class test-input.txt
	$(JAVA) $(LEXER) test-input.txt > output.txt

compile: $(LEXER).class
	
$(LEXER).class: $(LEXER).java FunkyReader.java ZeroReader.java
	$(JAVAC) $^

$(LEXER).java: zero-lexer.jflex
	$(JFLEX) $<

clean:
	rm -f *.class
	rm -f *~
	rm -f $(LEXER).java
	rm -f output.txt
