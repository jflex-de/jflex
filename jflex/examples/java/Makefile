# You need CUP v0.10j (or newer) for this makefile to work (for java12.cup)
#
# CUP classes should be included in CLASSPATH 

CUP        = java java_cup.Main -interface <
JFLEX      = jflex
JAVAC      = javac
#JAVAC      = jikes 
JAVACFLAGS = 

# --------------------------------------------------

all: compile

compile: scanner parser unicode
	$(JAVAC) $(JAVACFLAGS) JavaParser.java

parser: parser.java 

parser.java: java12.cup
	$(CUP) java12.cup

scanner: Scanner.java

Scanner.java: java.flex
	$(JFLEX) java.flex

unicode: UnicodeEscapes.java

UnicodeEscapes.java: unicode.flex
	$(JFLEX) unicode.flex

clean:
	rm -f *.class
	rm -f *~
	rm -f Scanner.java
	rm -f parser.java
	rm -f sym.java
	rm -f UnicodeEscapes.java
