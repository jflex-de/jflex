# only works with the Java extension of yacc: 
# byacc/j from http://byaccj.sourceforge.net/

JFLEX  = ../../bin/jflex 
BYACCJ = yacc -J

# targets:

all: Parser.class

run: Parser.class
	java Parser

build: clean Parser.class

clean:
	rm -f *~ *.class *.java

Parser.class: Yylex.java Parser.java
	javac Parser.java

Yylex.java: calc.flex
	$(JFLEX) calc.flex

Parser.java: calc.y
	$(BYACCJ) calc.y
