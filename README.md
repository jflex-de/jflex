[![Build Status](https://travis-ci.org/jflex-de/jflex.svg?branch=master)](https://travis-ci.org/jflex-de/jflex)

# JFlex

[JFlex][jflex] is a lexical analyzer generator (also known as scanner generator) for Java.

A lexical analyzer generator takes as input a specification with a set of regular expressions and
corresponding actions. It generates a program (a lexer) that reads input, matches the input against
the regular expressions in the spec file, and runs the corresponding action if a regular expression 
matched. Lexers usually are the first front-end step in compilers, matching keywords, comments, 
operators, etc, and generating an input token stream for parsers.

JFlex lexers are based on deterministic finite automata (DFAs).
They are fast, without expensive backtracking.

## Usage

For documentation and more information see the [JFlex web site][jflex]
and the [wiki][wiki].

### Usage with Maven

1. Place grammar files in `src/main/flex/` directory.

2. Extend the project [POM build section][pom-build] with the `maven-jflex-plugin`
  ```xml
    <build>
      <plugins>
        <plugin>
          <groupId>de.jflex</groupId>
          <artifactId>jflex-maven-plugin</artifactId>
          <version>1.6.1</version>
          <executions>
            <execution>
              <goals>
                <goal>generate</goal>
              </goals>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
  ```

3. Voilà: Java code is produced in `target/generated-sources/` during the `generate-sources` phase
(which happens before the `compile` phase) and included in the compilation scope.

Sample project: [simple-maven][example-simple-maven]

## Contributing

This is the JFlex git repository. The top level directory contains:

 * **cup** A copy of the CUP runtime
 * **docs** the Markdown sources for the user manual
 * **jflex** JFlex, the scanner/lexer generator for Java
 * **jflex-maven-plugin** the JFlex maven plugin, that helps to integrate JFlex in your project
 * **jflex-unicode-plugin** the JFlex unicode maven plugin, used for compiling JFlex
 * **testsuite** the regression test suite for JFlex,

## Open source

JFlex is free software, contributions are welcome.
See the file [CONTRIBUTING.md](CONTRIBUTING.md) for instructions.

[jflex]: http://jflex.de/
[wiki]: https://github.com/jflex-de/jflex/wiki
[pom-build]: https://maven.apache.org/pom.html#Build_Settings
[example-simple-maven]: https://github.com/jflex-de/jflex/tree/master/jflex/examples/simple-maven
