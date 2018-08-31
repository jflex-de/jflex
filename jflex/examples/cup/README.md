JFlex with cup
==============

This directory contains a small example of integration
between JFlex and [CUP][cup].

It comes from a [short article series in the Linux Gazette][1]
by Richard A. Sevenich and Christopher Lopes,
titled _Compiler Construction Tools_.

Small changes and updates to newest JFlex+Cup versions by Gerwin Klein

## Build and run

To compile:

```
jflex lcalc.flex
java java_cup.Main < ycalc.cup
javac Main.java
```

To run:

```
java Main test.txt
```

## Files:

* `Main.java`       demo of a main program
* `Makefile`        makefile to compile and test the example
* `lcalc.flex`      the lexer spec
* `output.good`     how the output should look like for the test
* `ycalc.cup`       the parser spec
* `test.txt`        sample input for testing

[cup]: http://www2.cs.tum.edu/projects/cup/
[1]: http://www.linuxgazette.com/issue39/sevenich.html
