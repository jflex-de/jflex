This directory contains the source code for JFlex.

Directory contents:

../pom.xml:
  Maven Project object model
  Useful targets:
	  mvn compile: build and compile JFlex
	  mvn test: run unit tests
    mvn package: generate jflex.jar
  
  
skeleton:
  a skeleton file matching exactly the precompiled scanner skeleton.
  Can be used together with the --skel option 

skeleton.nested:
  a skeleton file that supports nested input streams (see the manual 
  for the API). Can be used together with the --skel option

main/java/jflex:
  source files of package JFlex

main/java/jflex/gui:
  source files of package JFlex.gui

main/java/jflex/anttask
  source files of the JFlex Ant task (contributed by Rafal Mantiuk)
  
test/java/jflex
  unit tests for JFlex

main/java/java_cup/runtime:
  CUP v0.11a runtime classes, used by JFlex

