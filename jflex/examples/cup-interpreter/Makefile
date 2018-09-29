# uses JFlex >= 1.3.2, and CUP >= 0.10j
#
# targets:
#
# make all (or just: make)
#    generates lexer, and parser, and compiles all *.java files
#                                                                     
# make test
#    starts the program on a test example
#                                                                     

ROOT       = ../../..
JFLEX      = $(ROOT)/jflex/bin/jflex
CUPJAR     = $(ROOT)/cup/cup/java-cup-11b.jar
CUP        = java -jar $(CUPJAR)

all: clean compile

clean:
	rm -rf out
	mkdir -p out/genfiles

compile: gen_cup gen_jflex src/main/java/*.java
	javac -cp $(CUPJAR):out -d out src/main/java/*.java out/genfiles/*.java

# writes parser.java	sym.java
gen_jflex: src/main/jflex/scanner.flex
	$(JFLEX) -d out/genfiles $<

gen_cup: src/main/cup/parser.cup
	$(CUP) -interface < $<
	mv parser.java sym.java out/genfiles

output: compile src/test/data/example.as
	java -cp $(CUPJAR):out Main < src/test/data/example.as > out/output.txt

test: output
	@(diff out/output.txt src/test/data/output.good && echo "Test OK!") || (echo "Test FAILED"; exit 1)
