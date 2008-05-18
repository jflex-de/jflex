
all: compile

run: compile
#	java -classpath classes jflextest.Main $(HOME)/JFlex/lib/JFlex.jar
# java -classpath classes jflextest.Main $(HOME)/src/java/jflex-sf/jflex/lib/JFlex.jar
	java -classpath classes:/home/kleing/eclipse/plugins/org.apache.ant_1.7.0.v200706080842/lib/ant.jar:/home/kleing/workspace/jflex/lib/JFlex.jar:/usr/java/jdk1.5.0_09/lib/tools.jar jflextest.Main

compile: classes/jflextest/Main.class

clean:
	rm -f *~ jflextest/*~ jflextest/*.class jflextest/TestLoader.java  

classes/jflextest/Main.class: jflextest/TestLoader.java jflextest/*.java
	javac -d classes jflextest/*.java

jflextest/TestLoader.java: jflextest/TestLoader.flex
	jflex jflextest/TestLoader.flex

testclean:
	rm -f `find testcases -type f -name "*~"`
	rm -f `find testcases -type f -name "*.class"`
