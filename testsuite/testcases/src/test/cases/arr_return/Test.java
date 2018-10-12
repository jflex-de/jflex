import java.io.InputStreamReader;

public class Test {
  public static void main(String argv[]) throws Exception {
    Arr scanner = new Arr(new InputStreamReader(System.in));
    scanner.yylex();
  }
}
