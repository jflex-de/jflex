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

## Build systems

All examples (try to) support multiple build systems:
- [Maven](https://maven.apache.org/).
- [ant](https://ant.apache.org/).
- [bazel](https://bazel.build/).
- [make](https://www.gnu.org/software/make). 

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

### Maven

When the example can be build with Maven, there is a `pom.xml`.

Please use `mvn package` to
- generate the Java source from flex and cup definitions
- compile all Java source
- run unit tests

Build artifacts are in `target` (in subdirectories depending on their nature).

In the end run the compiled lexer with:
`java -jar target/` _BUILD-ARTEFACT.jar_ `<args>`

### ant

When the example can be build with Maven, there is a `build.xml`.

We place build artifacts in the `build` directory.

Also, we consistently use:
- `ant` _default action_ for **compile**
- `ant compile` to generate the source code form flex and cup definitions
   and compile all Java code
- `ant run` to run the lexer on a sample input
- `ant test` to run the lexer on the sample input and check it produces
  the expected output.

### Bazel

When the example can be build with Bazel, there is a `BUILD` file.

Bazel places artifacts in `bazel-*` in the root `examples` directory
(because it is the workspace).

- `bazel build <targets>` generates the sources and compile for the given targets
- `bazel run <target>`  runs the given target
- `bazel test <targets>` tests the given targets

See also [third_party/de/jflex/README.md].

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
