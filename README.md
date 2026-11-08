<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

[![Build](https://github.com/jflex-de/jflex/actions/workflows/build.yml/badge.svg)](https://github.com/jflex-de/jflex/actions/workflows/build.yml)

# JFlex

[JFlex][jflex] is a lexical analyzer generator (also known as scanner generator) for Java.

JFlex takes as input a specification with a set of regular expressions and corresponding actions.
It generates Java source of a lexer that reads input, matches the input against the regular
expressions in the spec file, and runs the corresponding action if a regular expression
matched. Lexers usually are the first front-end step in compilers, matching keywords, comments,
operators, etc, and generating an input token stream for parsers.

JFlex lexers are based on deterministic finite automata (DFAs).
They are fast, without expensive backtracking.


## Usage

For documentation and more information see the [JFlex documentation][jflex-doc]
and the [wiki][wiki].

### Usage with Maven

[![Maven central](https://img.shields.io/maven-central/v/de.jflex/jflex.svg)](https://search.maven.org/artifact/de.jflex/jflex/)

You need [Maven][maven] 3.5.2 or later, and JDK 8 or later.

1. Place grammar files in `src/main/flex/` directory.

2. Extend the project [POM build section][pom-build] with the `maven-jflex-plugin`

    ```xml
      <build>
        <plugins>
          <plugin>
            <groupId>de.jflex</groupId>
            <artifactId>jflex-maven-plugin</artifactId>
            <version>1.9.0</version>
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

### Usage with ant

You need ant, the binary JFlex jar and JDK 8 or later.

1. Define ant task

    ```xml
    <taskdef classname="jflex.anttask.JFlexTask" name="jflex"
            classpath="path-to-jflex.jar"/>
    ```

2. Use it

    ```xml
    <jflex file="src/grammar/parser.flex" destdir="build/generated/"/>
    <javac srcdir="build/generated/" destdir="build/classes/"/>
    ```

### Usage in CLI

You need the binary jflex jar and JDK 8 or later.

You can also use JFlex directly from the command line:

```sh
jflex/bin/jflex src/grammar/parser.flex
```

Or:

```sh
java -jar jflex-full-1.9.0.jar -d output src/grammar/parser.flex
```

### Other build tools

See [Build tool plugins](https://github.com/jflex-de/jflex/wiki/Build-tool-integration).


## Examples

Have a look at the sample project: [simple][example-simple] and other [examples].

## Contributing

[![Javadoc](https://javadoc.io/badge2/de.jflex/jflex/javadoc.svg)](https://javadoc.io/doc/de.jflex/jflex)

JFlex is free software, contributions are welcome.
See the [Contributing][contrib] page for instructions.

### Source layout

The top level directory of the JFLex git repository contains:

- **cup** A copy of the CUP runtime
- **cup-maven-plugin** A simple Maven plugin to generate a parser with CUP.
- **docs** the Markdown sources for the user manual
- **java** development tools: test framework and Unicode generators
- **javatests** regression tests and tests of the development tools
- **jflex** JFlex, the scanner/lexer generator for Java
- **jflex-maven-plugin** the JFlex maven plugin, that helps to integrate JFlex in your project
- **jflex-unicode-plugin** the JFlex unicode maven plugin, used for compiling JFlex
- **testsuite** the regression test suite for JFlex,


### Build from source

#### Build with Maven

You need JDK 8 or later.

```sh
./mvnw install
```

This generates `jflex/target/jflex-full-1.9.1-SNAPSHOT.jar` that you can use, e.g.

```sh
java -jar jflex-full-1.9.1-SNAPSHOT.jar --info
```


[jflex]: http://jflex.de/
[jflex-doc]: http://jflex.de/manual.html
[wiki]: https://github.com/jflex-de/jflex/wiki
[pom-build]: https://maven.apache.org/pom.html#Build_Settings
[example-simple]: https://github.com/jflex-de/jflex/tree/master/jflex/examples/simple
[examples]: https://github.com/jflex-de/jflex/tree/master/jflex/examples/
[contrib]: https://github.com/jflex-de/jflex/wiki/Contributing
[maven]: https://maven.apache.org/
