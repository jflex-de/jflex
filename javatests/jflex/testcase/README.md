# JFlex test suite

## Run the test suite

To run all regression tests:

    bazel test //javatests/jflex/testcase/...
    
## Migration from Maven to Bazel

There are only a few tests, as their migration from Bazel is work in progress.

## Migrate a test

If you want to contribute, here is how to migrate a test from
[testsuite/testcases](https://github.com/jflex-de/jflex/tree/master/testsuite/testcases)
to
[javatests/jflex/testcase](https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase).

There are just a few steps to migrate a test,
and [testcase/bol](https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase/bol)
is a simple example.

1. Generate a scanner from the flex file with the `jflex` rule, by creating a
   build target.
   - Example for `bol.flex`:
     ```python
     jflex(
         name = "gen_bol_scanner", # follow the convention gen_<filename>_scanner
         srcs = ["bol.flex"],      # the source itself
         jflex_bin = "//jflex:jflex_bin", # Important!
         outputs = ["BolScanner.java"], # as defined byy the class
     )
     ```
   - **Important** Be sure to point to the current `jflex_bin`, otherwise the
     test will run against the (previous) released version.
   - Also, define the java package corresponding to the directory of this test
     ```text
     package jflex.testcase.bol;
     %%
     // rest of the grammar
     ```
2. Define a scanner state.
   - With the `jflex-testsuite-maven-plugin`, the test was defined with an input
     file and an expected output on `System.out` where each lexer action was
     using a `System.println` statement.
     A test should be on the API, not on the console debug output, so we will
     migrate to unit tests.
   - Update the flex definition to output a state instead.
     ```text
     %type State
     ```
   - Create a `State.java` enum with the states
     ```java
     public enum State {
       HELLO_AT_BOL_AND_EOL,
       HELLO_AT_EOL,
     }
     ```
   - Modify the flex grammar accordingly.
     Replace
     ```text
     ^"hello"$  { System.out.println("hello at BOL and EOL"); }
     "hello"$   { System.out.println("hello at BOL"); }
     ```
     by the newly defined states
     ```text
     ^"hello"$  { return State.HELLO_AT_BOL_AND_EOL; }
     "hello"$   { return State.HELLO_AT_EOL; }
     ```
   - Optionally, also add an EOF state in the grammar:
     ```text
     <<EOF>>    { return State.END_OF_FILE; }
     ```
3. Use unit tests to verify the scanner.
   - Add a standard jUnit class, e.g. `BolTest.java`.
   - Create tests from the old `.input` and `.output` files, as deemed
     appropriate.
     For instance, the line
     ```text
     ␣␣hello
     ```
     with expected output
     ```text
     line: 2 col: 1 char: 6 match: -- --
     action [27] { System.out.println( "\" \"" ); }
     " "
     line: 2 col: 2 char: 7 match: -- --
     action [27] { System.out.println( "\" \"" ); }
     " "
     line: 2 col: 3 char: 8 match: --hello--
     action [21] { System.out.println("hello at EOL"); }
     hello at EOL
     line: 2 col: 8 char: 13 match: --\u000A--
     action [25] { System.out.println("\\n"); }
     \n
     ```
     could be migrated to a single test
     ```java
       @Test
       public void eol() throws Exception {
         scanner = createScanner("  hello\n");
         assertThat(scanner.yylex()).isEqualTo(State.SPACE);
         assertThat(scanner.yylex()).isEqualTo(State.SPACE);
         assertThat(scanner.yylex()).isEqualTo(State.HELLO_AT_EOL);
         assertThat(scanner.yylex()).isEqualTo(State.LINE_FEED);
       }
     ```
     - Please use Google truth to write the assertions.
4. Add test target.
   - Add a the corresponding `java_test`.
   - It's ok to have all sources in this target.
     You don't need to create a `java_library` just for the scanner.
     ```python
        java_test(
            name = "BolTest",
            srcs = [
                "BolTest.java",
                "State.java",
                ":bol_scanner",
            ],
            deps = [
                "//third_party/com/google/truth",
            ],
        )
      ```
   

### Migrating a test where the scanner generation fails

Of course, this cannot be applied if the generation of the scanner is expected
to fail: We can't break the build to make a test pass.

In that case, you can use a custom `JflexTestRunner`.

Example: [EofPipeActionTest](https://github.com/jflex-de/jflex/blob/master/javatests/jflex/testcase/action_pipe/EofPipeActionTest.java).
