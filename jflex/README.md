# JFlex 1.7.0

This directory contains JFlex, a fast scanner generator for Java.

To run JFlex, run `bin/jflex` from the command line or double click on the
JFLex jar file in the `lib/` directory. You need JDK 1.7 installed and set up.

See the manual in `doc/` or the website at <http://jflex.de> for more
information and for how to get started.


## Contents ##

      bin/                    command line start scripts
      doc/                    manual
      examples/               some example scanners
      lib/                    JFlex jar file, syntax highlighting files
      src/                    JFLex sources
     
      pom.xml                 Maven project object model, useful commands:
                                 mvn compile   build and compile JFlex
                                 mvn test      run unit tests
                                 mvn package   generate the JFlex jar
                                 
      build.xml               Ant build file, useful commands:
                                 ant gettools  get tools to run Ant build
                                 ant compile   build and compile JFlex
                                 ant test      run unit tests
                                 ant jar       generate the JFlex jar


## Dependencies ##

* To run JFlex, you need at least JDK 1.7.
* To build JFlex, you need JDK 1.7+ and Maven 3.
  Maven will take care of the remaining dependencies, such as JFlex, 
  CUP, JUnit, etc.
* You can also build JFlex with Ant 1.8+.  To run unit tests you need
  ant-junit.jar installed in $ANT_HOME/lib/.
