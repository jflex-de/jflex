package jflex.testcase.ccl_esc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Sample {
  private Sample() {}

  public static void main(String argv[]) throws IOException {
    Reader reader = new InputStreamReader(System.in);
    Ccl yy = new Ccl(reader);
    Yytoken t;
    while ((t = yy.yylex()) != null) System.out.println(t);
  }
}
