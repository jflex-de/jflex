package jflex.testcase.apipirivate;

import java.io.InputStreamReader;

public class AttemptPrivateAccess {

  public static void main(String argv[]) {
    try {
      Private s = new Private(new InputStreamReader(System.in));
      s.yylex();
    } catch (Exception e) {
    }
  }
}
