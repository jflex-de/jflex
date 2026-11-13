<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# @TestSpec and JFlexTest

The `JFlexTest` instance of the JUnit 5 `@TestSpec` annotation can generate and
compile scanners before the test runs. This also allows testing the situation
where the lexer generation is expected to fail, or the generated code should not
compile.

    @TestSpec(lex = "src/test/java/de/jflex/testcase/foo/foo.flex")
    public class FooTest implements JFlexTest {}

`JFlexTest` adds the tests "Generate Lexer" and, unless the spec expects JFlex
to fail, "Compile java code" to the class, which report these two steps. The
class can have further tests that use the scanner. These are skipped if one of
the steps fails.

Supported:

- generating the lexer
- passing options such as `--jlex` or `-q` to jflex
- using a custom skeleton file (`skeleton`)
- verifying that the generated lexer compiles
- checking that the generation fails, with the expected exception and cause
- checking the number of states of the minimized DFA
  (`minimizedDfaStatesCount`), and checking the canonical minimized DFA if
  provided (`minimizedDfa`)
- checking the number of character classes, and the classes themselves
  (`charClassesCount`, `charClasses`)
- checking that given characters are in the same or in different character
  classes (`sameCharClass`, `distinctCharClasses`)
- comparing the output logs with a golden file
