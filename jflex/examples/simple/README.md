# Simple usage

This is a very simple example of how to generate a lexer with JFlex:

- for a very simple grammar of the _Toy programmimg language_
  described in the user manual.
- without integration to a parser. As a result, the program
  does nothing really useful, because there is no parser.

The generated lexer has the default name **Yylex** because the flex
specification doesn't define a name with the `%class`.

The project comes with a test class for the lexer: `YylexTest`.
The test:
- runs the lexer in debug mode on `test.txt`
- collects the output of JFlex by redirecting `System.out`
- and verifies that the verbose logs of JFlex corresponds to 
  the expected content of `output.good`.

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

## Compile and test

### Using Maven

**Tip** Jflex comes with the Maven wrapper.
You can use `../../mvnw` instead of `mvn` if the later is not installed on your
system.

#### Generate the lexer

    mvn generate-sources
     
The **jflex-maven-plugin** will read the grammar `src/main/jflex/simple.jflex`
and generate a Java scanner **Yylex** in
`target/generated-sources/flex/Yylex.java`.

This is defined by the following section
```xml
  <build>
    <plugins>
      <plugin>
        <groupId>de.jflex</groupId>
        <artifactId>jflex-maven-plugin</artifactId>
        <version>1.8.0</version>
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

By default, the **jflex-maven-plugin** generates a lexer (scanner) for every file
in `src/main/jflex/`.

#### Build

    mvn compile
    
The compile phase will build all Java classes, including those generated automatically.

You will find the class files in `target/classes`.

**Tip** In fact, you don't have to invoke `mvn generate-sources` explicitly,
the **compile** phase will do it automatically.

#### Test

    mvn test
    
The **test** phase executes the test in `src/test/java`.

	This goal test the generated scanner (if required, the lexer will be 
	generated and all Java classes will be compiled)
	by running all tests in <<<src/test/java>>>.
	
	There is only one test in <<<src/test/java/YylexTest.java>>>.
	In this test, 
	the scanner is run with the input file <<<src/test/resources/test.txt>>>.
	
	By default, the scanner outputs debugging information about each 
	returned token to <<<System.out>>> until the end of file is reached, 
	or an error occurs.
	But in the test, the output is redirected into <<<target/output.actual>>>.
	
	The test is successful if every line match
	with <<<src/test/resources/output.good>>>,
	which is the expected scanner debugging information.
	
	
    ../../mvnw package

Expected output:
* `target/generated-sources/jflex/Yylex.java` Java code generated from the flex file.

**Rem** Notice that Maven ran a test for you. 

### Build with Bazel

#### Build

    blaze build //simple

Expected output:
* (`examples/`)`bazel-genfiles/simple/Yylex.java` Java code generated from the flex file.

#### Test 

To execute the tests

    blaze test //simple/...

To run the lexer on any file

    bazel run //simple:simple_bin -- /full/path/to/src/test/resources/test.txt
    
**N.B.** Relative path doesn't work in `bazel run`.

**Rem:** The Bazel commands work from any directory in the workspace.

Alternatively, use the generated artifact. From the `examples` directory:

    bazel-bin/simple/simple_bin simple/src/test/resources/test.txt
    