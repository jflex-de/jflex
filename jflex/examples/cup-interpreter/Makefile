# uses JFlex >= 1.3.2, and CUP >= 0.10j
#
# targets:
#
# make all                                                            
#    generates lexer, and parser, and compiles all *.java files
#                                                                     
# make run (or just: make)                                           
#    starts the program on a test example
#                                                                     

JAVA=java
JAVAC=javac
# Root of the project
ROOT=../../..
JFLEX=$(ROOT)/jflex/bin/jflex
CUPJAR=$(ROOT)/cup/cup/java-cup-11b.jar
CUP=$(JAVA) -jar $(CUPJAR)
CP=.:$(CUPJAR)

default: test

.SUFFIXES: $(SUFFIXES) .class .java

.java.class:
	$(JAVAC) -cp $(CP) $*.java

FILE=	Yylex.java      parser.java    sym.java \
	SymTab.java	AST.java \
	Tnumber.java	Tident.java	Texp.java	\
	Tfun.java	Texpinfix.java	Tuminus.java	\
	Tboolexp.java	Tifthenelse.java		\
	Tdekl.java	Tdekllist.java			\
	Tparlist.java   Texplist.java	Tprogram.java	\
	Main.java \
	SymtabEntry.java STEfun.java	STEvar.java

run: output.txt

output.txt: all
	$(JAVA) -cp $(CP) Main < example.as > output.txt
	cat output.txt

all: Yylex.java parser.java $(FILE:java=class)

clean:
	rm -f *.class *~ *.bak Yylex.java parser.java sym.java

Yylex.java: scanner.flex
	$(JFLEX) scanner.flex

parser.java: parser.cup
	$(CUP) -interface < parser.cup

test: output.txt
	@(diff output.txt output.good && echo "Test OK!") || echo "Test failed!"
