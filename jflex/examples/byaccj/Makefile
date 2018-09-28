# Be sure to use yacc/j ; not unix yacc nor bison.
BYACCJ = yaccj -J
JFLEX  = ../../bin/jflex

run: all
	java -cp target Parser

all: build

build: Parser.class

clean:
	rm -f target

Parser.class: Yylex.java Parser.java
	javac -d target target/*.java

Yylex.java: src/main/jflex/calc.flex
	$(JFLEX) -d target $<

Parser.java: src/main/yacc/calc.y
	$(BYACCJ) $< ;  mv Parser.java ParserVal.java target
