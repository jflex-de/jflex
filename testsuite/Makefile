
JFLEX_TEST=$(HOME)/workspace/jflex/lib/JFlex.jar

JFLEX_BUILD=$(HOME)/src/java/jflex-stable/lib/JFlex.jar
JAVAC_JAR=/System/Library/Frameworks/JavaVM.framework/Classes/classes.jar
JUNIT_JAR=$(HOME)/eclipse/plugins/org.junit_3.8.2.v200706111738/junit.jar
ANT_JAR=$(HOME)/eclipse/plugins/org.apache.ant_1.7.0.v200706080842/lib/ant.jar

CLASSES=classes

COMPILE_CP=$(CLASSES):$(ANT_JAR):$(JUNIT_JAR):$(JFLEX_BUILD)
RUN_CP=$(CLASSES):$(ANT_JAR):$(JUNIT_JAR):$(JFLEX_TEST)

all: compile

run: compile
	java -classpath $(RUN_CP) jflextest.Main

compile: classes/jflextest/Main.class

clean:
	rm -f *~ jflextest/*~ jflextest/*.class jflextest/TestLoader.java  

classes/jflextest/Main.class: jflextest/TestLoader.java jflextest/*.java
	mkdir -p $(CLASSES)
	javac -d $(CLASSES) -classpath $(COMPILE_CP) jflextest/*.java

jflextest/TestLoader.java: jflextest/TestLoader.flex
	jflex jflextest/TestLoader.flex

testclean:
	rm -f `find testcases -type f -name "*~"`
	rm -f `find testcases -type f -name "*.class"`
