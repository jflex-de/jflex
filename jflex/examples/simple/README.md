# Simple usage

This is a very simple example of how to generate a lexer
- for a very simple grammar
- without integration to a parser

## Build with maven

    ../../mvnw package

Expected output:
* `target/generated-sources/jflex/Yylex.java` Java code generated from the flex file.

**Rem** Notice that Maven ran a test for you. 

## Build with Bazel

    blaze build //simple

Expected output:
* (`examples/`)`bazel-genfiles/simple/Yylex.java` Java code generated from the flex file.

To execute the tests

    blaze test //simple/...

To run the lexer on any file

    bazel run //simple:simple_bin -- /full/path/to/src/test/resources/test.txt
    
**N.B.** Relative path doesn't work in `bazel run`.

**Rem:** The Bazel commands work from any directory in the workspace.

Alternatively, use the generated artifact. From the `examples` directory:

    bazel-bin/simple/simple_bin simple/src/test/resources/test.txt
    
## Files

* `main/flex/simple.flex`:
  the simple grammar specification
* `test/resources/test.txt`:
  sample input
* `test/resources/output.good`:
  _golden file_, i.e. expected output corresponding to the sample input from `test.txt`
* `tests/java/YylexTest.java`:
   jUnit integration test that running the lexer on the sample input produces
   the same output as the _golden file_.
