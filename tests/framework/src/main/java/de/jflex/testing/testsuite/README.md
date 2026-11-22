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
- checking the warnings of JFlex (`warnings`), by message code and optionally file,
  line and column:

      warnings = {
        @Warn(ErrorMessages.MACRO_UNUSED),
        @Warn(value = ErrorMessages.NEVER_MATCH, line = 17)
      }

  A spec without the `warnings` annotation must not produce any warning.
- comparing the output log of JFlex with a golden file (`sysout`), with the
  expected lines inline (`output`), or checking that JFlex prints nothing
  (`noOutput`). Prefer `warnings` for warnings, because it does not depend on
  the text of the messages.

## ScannerTest

Tests that check the tokens of the generated scanner one by one can extend
`de.jflex.testing.scan.ScannerTest`, instantiating the type parameters to
the scanner class and the token type.

    @TestSpec(lex = "src/test/java/de/jflex/testcase/look/look.flex", quiet = true)
    public class LookTest extends ScannerTest<Look, Integer> implements JFlexTest {
      public LookTest() {
        super(Look::new, Look::yylex, Look.YYEOF);
      }

      @Test
      public void shortBase() throws Exception {
        scan("abcde\n");
        assertToken(AB, "ab", 0, 0);
        assertEach(OTHER, "cde\n", 0, 2);
        assertEof();
      }
    }

- `scan(input)` creates a new scanner, which can also be accessed later via
  the field `scanner`, e.g. for `scanner.yybegin()` or `scanner.yystate()`.
- `assertToken(token, text)` checks the token, the text and the length of the
  next match. Variations exist for `line, column` and `line, column, offset`.
- `assertEach(token, text)` checks one match with the token for every char of
  the text, `assertEach(token, text, line, column)` with the column increased
  for each match.
- `assertEof()` checks that `yylex()` returns the end-of-input value of the
  constructor and that `yyatEOF()` is true.
- `Chars.hex(codePoint)` and `Chars.hex(text)` produce `U+0041` and `\u0041` for
  messages.

Easiest use is with `%implements` and `%counter_methods`:

    %implements de.jflex.testing.scan.TestScanner
    %line
    %column
    %counter_methods

Specs that do not want `%implements` or `%counter_methods` can override the
methods of `ScannerTest` directly (`yytext()`, `yylength()`, `yyatEOF()`,
`yyline()`, `yycolumn()`, `yychar()`), for instance

    @Override
    protected int yyline() {
      return scanner.line();
    }

A test class where the length check of `assertToken` is not wanted can override
`checkLength()` to return `false`. To leave out line/column counting, use only
the assertions without line/column.
