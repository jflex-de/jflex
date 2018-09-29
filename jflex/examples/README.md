# JFlex examples

All examples support multiple build systems:
- [Maven](https://maven.apache.org/). Build artifacts are in `target` (in subdirectories depending on their nature)
- [ant](https://ant.apache.org/). We place build artifacts in `build`.
- [make](https://www.gnu.org/software/make). We place build artifacts in `out`.

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
