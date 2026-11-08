<!--
  Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# JFlex examples

## The Examples

* **byaccj**:
  integration between JFlex and byacc/j on a simple parser for an arithmetic calculator
* **cup-interpreter**:
  integration between JFlex and CUP on a language interpreter
* **cup-java**:
  integration between JFlex and CUP on a a Java parser
* **cup-java-minijava**:
  example from the JFlex manual
* **cup-lcalc**:
  integration between JFlex and CUP on a simple parser for a arithmetic calculator
* **simple**:
  simple lexer with no parser integration
* **standalone**:
  "hello world" parser for which the `main` is generated automatically
* **zero-reader**:
  shows how to work with input Readers that sometimes return 0 characters.

Every example also provides its own `README.md` with more context.


## Directory structure

All examples follow the Maven layout, in particular:
- `src` contains the source files
  - `src/main` contains the source files for the actual program
    - `src/main/java` contains the Java sources
    - `src/main/jflex` contains the flex definitions that should be used by
      JFlex to generate java code
    - `src/main/cup` contains the CUP definitions that should be used by CUP
  - `src/test` contains the source files for tests
    - `src/test/data` contains an input file for the scanner; as well as a
      _golden file_ of the expected output for this input.


## Build systems

All examples (try to) support multiple build systems:
- [Maven](https://maven.apache.org/).
- [ant](https://ant.apache.org/).
- [make](https://www.gnu.org/software/make).

### Prerequisites

- [Maven](https://maven.apache.org/)

  - Install Java Runtime
  - Install [maven](https://maven.apache.org/).

- [ant](https://ant.apache.org/)

  - Install Java Development Kit
  - Install [Apache ant](https://ant.apache.org/bindownload.cgi)

- [GNU make](https://www.gnu.org/software/make)

  - Install make
  - Install Java Development Kit


## Usage

### Maven

When the example can be built with Maven, there is a `pom.xml`.

Please run `mvn package` to:

- generate the Java source from flex and cup definitions
- compile all Java source
- run unit tests

Build artifacts are in `target` (in subdirectories depending on their nature).

Run `mvn test` to execute and test the generated lexer.

To manually run the compiled lexer type:
`java -jar target/` _BUILD-ARTEFACT.jar_ `<args>`


### ant

For examples that can be built with ant, there is a file `build.xml`.

We place build artifacts in the `antbuild` directory.

Also, we consistently use:
- `ant` _default action_ for **test**
- `ant compile` to generate the source code form flex and cup definitions
   and compile all Java code
- `ant run` to run the lexer on a sample input
- `ant test` to run the lexer on the sample input and check whether it
  produces the expected output.


### GNU make

Examples that can be built with `make` have a file `Makefile`.

We place build artifacts in the `out` directory.

Also, we consistently use:
- `make` _default action_ for **test**
- `make compile` to generate the source code form flex and cup definitions
   and compile all Java code
- `make run` to run the lexer on a sample input
- `make test` to run the lexer on the sample input and check whether it
  produces the expected output.
