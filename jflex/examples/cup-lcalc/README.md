JFlex with cup
==============

This directory contains a small example of integration
between JFlex and [CUP][cup].

It comes from a [short article series in the Linux Gazette][1]
by Richard A. Sevenich and Christopher Lopes,
titled _Compiler Construction Tools_.

Small changes and updates to newest JFlex+Cup versions by Gerwin Klein.

## Build and run

To compile:

```
mvn compile test
```

This will build and test that the `src/test/resources/test.txt` file
is parsed and generates the expected `src/test/resources/output.good`.

To run:

```
mvn package
java -jar target/simple-maven-full-1.0.jar Main test.txt
```

## Files:

* `src/main/java/Main.java`         demo of a main program
* `src/main/flex/lcalc.flex`        the lexer spec
* `src/main/cup/ycalc.cup`          the parser spec
* `src/test/resources/test.txt`     sample input for testing
* `src/test/resources/output.good`  how the output should look like for the test
* `pom.xml`                         the Maven Project model
  - The `jflex-maven-plugin` generates `Lexer.java` from `lcalc.flex`
  - The `cup-maven-plugin` generates `sym.class` from `ycalc.cup`
  
[cup]: http://www2.cs.tum.edu/projects/cup/
[1]: http://www.linuxgazette.com/issue39/sevenich.html
