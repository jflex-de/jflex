package jflex.testcase.ccl_esc;

public class Sample {
  public static void main(String argv[]) throws java.io.IOException {
    java.io.Reader reader = new java.io.InputStreamReader(System.in);
    Yylex yy = new Yylex(reader);
    Yytoken t;
    while ((t = yy.yylex()) != null) System.out.println(t);
  }
}
