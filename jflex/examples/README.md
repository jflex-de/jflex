# JFlex examples

## The Examples

* **byaccj**:
  integration between JFlex and byacc/j on a simple parser for an arithmetic calculator
* **cup-interpreter**:
  integration between JFlex and CUP on a complex interpreter
* **cup-java**:
  integration between JFlex and CUP on a a Java parser
* **cup-lcalc**:
  integration between JFlex and CUP on a simple parser for a arithmetic calculator
* **simple**:
  a simple lexer with no parser integration
* **standalone**:
  an "hello world" parser for which the `main` is generated automatically
* **zero-reader**:
  This example shows how to work with input Readers that sometimes return 0
  characters.

Every example also provides its own `README.md` with more context.


## Directory structure

All examples follow the Maven layout, in particular:
- `src` contains the source files
  - `src/main` contains the source files for the actual programm
    - `src/main/java` contains the Java sources
    - `src/main/jflex` contains the flex definitions that should be used by JFlex
      to generate java code
    - `src/main/cup` contains the CUP definitions that should be used by CUP  
  - `src/test` contains the source files for tests
    - `src/test/data` contains an input file for the scanner; as well as a _golden file_
      of the expected output for this input.


## Build systems

All examples (try to) support multiple build systems:
- [Maven](https://maven.apache.org/).
- [ant](https://ant.apache.org/).
- [bazel](https://bazel.build/).
- [make](https://www.gnu.org/software/make). 

### Prerequisites

- [Maven](https://maven.apache.org/)
  
  - Install Java Runtime
  - You don't need to install Maven, since the project ships the Maven wrapper.

- [ant](https://ant.apache.org/)
  
  - Install Java Development Kit
  - Install [Apache ant](https://ant.apache.org/bindownload.cgi)
  - Install [Apache ivy](http://ant.apache.org/ivy/history/2.5.0-rc1/install.html)

- [bazel](https://bazel.build/)

  - Install [Bazel](https://docs.bazel.build/versions/master/install.html)

- [GNU make](https://www.gnu.org/software/make)

  - Install make
  - Install Java Development Kit


## Usage

### Maven

When the example can be build with Maven, there is a `pom.xml`.

Please run `../mvnw package` to:

- generate the Java source from flex and cup definitions
- compile all Java source
- run unit tests

Build artifacts are in `target` (in subdirectories depending on their nature).

In the end run the compiled lexer with:
`java -jar target/` _BUILD-ARTEFACT.jar_ `<args>`


### ant

When the example can be build with ant, there is a `build.xml`.

We place build artifacts in the `antbuild` directory.

Also, we consistently use:
- `ant` _default action_ for **compile**
- `ant compile` to generate the source code form flex and cup definitions
   and compile all Java code
- `ant run` to run the lexer on a sample input
- `ant test` to run the lexer on the sample input and check it produces
  the expected output.


### Bazel

We test the examples with Bazel, after the `bazel.sh` has copied the SNAPSHOT JFlex in the
`examples` directory.

If you are interested in using the Skylark "jflex()" rule and see example, please see
[jflex-de/bazel_rules](https://github.com/jflex-de/bazel_rules).


### GNU make

When the example can be build with make, there is a `Makefile`.

We place build artifacts in the `out` directory.

Also, we consistently use:
- `make` _default action_ for **compile**
- `make compile` to generate the source code form flex and cup definitions
   and compile all Java code
- `make run` to run the lexer on a sample input
- `make test` to run the lexer on the sample input and check it produces
  the expected output.
