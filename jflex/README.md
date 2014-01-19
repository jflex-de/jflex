# JFlex 1.5.0

This directory contains JFlex, a fast scanner generator for Java.

To run JFlex, run `bin/jflex` from the command line or double click on
`lib/JFlex.jar`. You need JDK 1.5 installed and set up.

See the manual in `doc/` or the website at <http://jflex.de> for more
information and for how to get started.


## Contents ##

      bin/                    command line start scripts
      doc/                    manual
      examples/               some example scanners
      lib/                    JFlex.jar, syntax highlighting, skeleton files
      
      src/                    
      src/main/java/          JFLex sources
      src/main/jflex/         scanner spec
      src/main/cup/           parser spec
      src/main/resources/     strings and default skeleton file
      src/test/               unit tests
     
      parent.xml +                         
      pom.xml                 maven project object model, useful targets:
                                 mvn compile   build and compile JFlex
                                 mvn test      run unit tests
                                 mvn package   generate jflex.jar


## Dependencies ##

* To run JFlex, you need at least JDK 1.5.
* To build JFlex, you need JDK 1.5 and maven 2.2.1. 
  Maven will take care of the remaining dependencies, such as JFlex, 
  CUP, JUnit, etc.
