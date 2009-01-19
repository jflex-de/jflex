import java.io.*;

public class Test {

  public static void main(String argv[]) {
    try {
      Private s = new Private(new InputStreamReader(System.in));
      s.yylex();
    }
    catch (Exception e) {      
    }
  }

}
