[![Build Status](https://travis-ci.org/jflex-de/jflex.svg?branch=master)](https://travis-ci.org/jflex-de/jflex)

# JFlex

[JFlex][jflex] is a lexical analyzer generator (also known as scanner generator) for Java.

JFlex takes as input a specification with a set of regular expressions and corresponding actions.
It generates Java source of a lexer that reads input, matches the input against the regular
expressions in the spec file, and runs the corresponding action if a regular expression
matched. Lexers usually are the first front-end step in compilers, matching keywords, comments, 
operators, etc, and generating an input token stream for parsers.

JFlex lexers are based on deterministic finite automata (DFAs).
They are fast, without expensive backtracking.

## Modules

The top level directory of the JFLex git repository contains:

 * **cup** A copy of the CUP runtime
 * **cup-maven-plugin** A simple Maven plugin to generate a parser with CUP.
 * **docs** the Markdown sources for the user manual
 * **jflex** JFlex, the scanner/lexer generator for Java
 * **jflex-maven-plugin** the JFlex maven plugin, that helps to integrate JFlex in your project
 * **jflex-unicode-plugin** the JFlex unicode maven plugin, used for compiling JFlex
 * **testsuite** the regression test suite for JFlex,
 * **third_party** third-party librairies used by examples of the [Bazel build system][bazel]

## Usage

For documentation and more information see the [JFlex documentation][jflex-doc]
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
          <version>1.7.0</version>
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

You can also use JFlex directly from the command line:
```
jflex/bin/jflex src/grammar/parser.flex
```

Or:
```
java -jar jflex-full-1.7.0.jar -d output src/grammar/parser.flex
```

### Other build tools

See [Build tool plugins](https://github.com/jflex-de/jflex/wiki/Build-tool-plugins).

## Examples

Have a look at the sample project: [simple][example-simple] and other [examples].

## Build from source

```
./mvnw install
```

## Contributing

JFlex is free software, contributions are welcome.
See the [Contributing][contrib] page for instructions.


[jflex]: http://jflex.de/
[jflex-doc]: http://jflex.de/manual.html
[wiki]: https://github.com/jflex-de/jflex/wiki
[pom-build]: https://maven.apache.org/pom.html#Build_Settings
[example-simple]: https://github.com/jflex-de/jflex/tree/master/jflex/examples/simple
[examples]: https://github.com/jflex-de/jflex/tree/master/jflex/examples/
[contrib]: https://github.com/jflex-de/jflex/wiki/Contributing
[bazel]: http://bazel.build/
