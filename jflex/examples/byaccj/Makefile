# Be sure to use yacc/j ; not unix yacc nor bison.
BYACCJ = yaccj -J
JFLEX  = java -jar ../../out/jflex-full-1.7.1-SNAPSHOT.jar

run: all
	java -cp out Parser

all: build

build: Parser.class

clean:
	rm -f out

Parser.class: Yylex.java Parser.java
	javac -d out out/*.java

Yylex.java: src/main/jflex/calc.flex
	$(JFLEX) -d out $<

Parser.java: src/main/yacc/calc.y
	$(BYACCJ) $< ;  mv Parser.java ParserVal.java out
