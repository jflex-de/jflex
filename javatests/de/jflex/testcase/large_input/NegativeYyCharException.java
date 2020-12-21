package jflex.testcase.large_input;

/** Thrown when the lexer's {@code zzchar} is negative, which should never happen. */
public class NegativeYyCharException extends IllegalStateException {
  NegativeYyCharException(long yychar) {
    super("The scanner has a negative number of read characters: " + yychar);
  }
}
