# Simple usage

This is a very simple example of how to generate a lexer with JFlex:

- for a very simple grammar of the _Toy programming language_
  described in the user manual.
- without integration to a parser. As a result, the program
  does nothing really useful, because there is no parser.

The generated lexer has the default name **Yylex** because the flex
specification doesn't define a name with the `%class`.

The project comes with a test class for the lexer: `YylexTest`.

The test:

1. runs the lexer in debug mode on `test.txt`
2. collects the output of JFlex by redirecting `System.out`
3. and verifies that the verbose logs of JFlex corresponds to
   the expected content of `output.good`.


## Files

* `src/main/flex/simple.flex`:
  the simple grammar specification
* `src/test/data/test.txt`:
  sample input
* `src/test/data/output.good`:
  _golden file_, i.e. expected output corresponding to the sample input from `test.txt`
* `src/test/java/YylexTest.java`:
   jUnit integration test that running the lexer on the sample input produces
   the same output as the _golden file_.


## Compile and test

### Using Maven

#### Generate the lexer

    mvn generate-sources

The **jflex-maven-plugin** reads the grammar `src/main/jflex/simple.jflex`
and generates a Java scanner **Yylex**.

Expected output:

* `target/generated-sources/flex/Yylex.java`.

This is defined by the following section

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>de.jflex</groupId>
        <artifactId>jflex-maven-plugin</artifactId>
        <version>1.8.2</version>
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

By default, the **jflex-maven-plugin** generates a lexer (scanner) for every
file in `src/main/jflex/`.

#### Build

    mvn compile

The **compile** phase will generate the sources, and build all Java classes,
including those generated automatically.

Expected output:

* Java compiled class files in `target/classes`.

**Tip** In fact, you don't have to invoke `mvn generate-sources` explicitly,
the **compile** phase will do it automatically.

#### Test

    mvn test

The **test** phase does everything above and executes the test in `src/test/java`.

There is only one test in `src/test/java/YylexTest.java`.
In this test, the scanner is run with the input file `src/test/data/test.txt`.

By default, the scanner outputs debugging information about each returned
token to `System.out` until the end of file is reached, or an error occurs.
But in the test, the output is redirected into an in-memory output stream.

Then, test opens the _golden file_ `src/test/data/output.good` and compares
with the actual output.


#### Package

    mvn package

The **package** phase does everything above and packages the jar archive of the Java classes. You can then run

    java -jar target/simple-1.0.jar src/test/data/test.txt

to test the lexer.

### Using Ant

#### Build

    ant compile

is roughly equivalent to `mvn compile` above

#### Run on sample file

    ant run

will run the generated lexer on the provided sample input.

#### Test

    ant test

will run the same test as in `mvn test`.

### Using Bazel

Please see [bazel_rules/examples](https://github.com/jflex-de/bazel_rules).


### Using Make

    make compile

is roughly equivalent to `mvn compile`, and

    make test

will run generated lexer, comparing against expect output.