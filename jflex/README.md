# JFlex 1.7.1-SNAPSHOT

This directory contains JFlex, a fast scanner generator for Java.

To run JFlex, run `bin/jflex` from the command line or double click on the
JFLex jar file in the `lib/` directory.

See the manual in `doc/` or the website at <http://jflex.de> for more
information and for how to get started.


## Contents

    ├── build.xml        script to build with ant
    ├── changelog.md     summary of the changes
    ├── pom.xml          project object model to build with Maven
    ├── README.md        this file
    ├── bin              command line start scripts
    ├── [build]          build directory if you used ant
    ├── doc              user manual
    ├── examples         example scanners and parsers
    ├── lib              syntax highlighting files ; also JFlex jar in binary distribution
    ├── src              JFLex sources
    └── target

## Dependencies

* To run JFlex, you need at least JDK 1.7.
* To build JFlex, you need JDK 1.7+ and Maven 3.
  Maven will take care of the remaining dependencies, such as JFlex, 
  CUP, JUnit, etc.
* You can also build JFlex with Ant 1.8+.  To run unit tests you need
  ant-junit.jar installed in $ANT_HOME/lib/.
