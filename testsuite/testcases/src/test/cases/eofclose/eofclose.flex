
import java.io.*;

%%

%public
%class Eofclose
%int

%eofclose

%{
  public static void main(String argv[]) {
    int firstFilePos = 0;
    String encodingName = "UTF-8";
    if (argv[0].equals("--encoding")) {
      firstFilePos = 2;
      encodingName = argv[1];
      try {
        java.nio.charset.Charset.forName(encodingName); // Side-effect: is encodingName valid?
      } catch (Exception e) {
        System.out.println("Invalid encoding '" + encodingName + "'");
        return;
      }
    }
    for (int i = firstFilePos; i < argv.length; i++) {
      Eofclose scanner;
      Reader reader = null;
      try {
        FileInputStream stream = new FileInputStream(argv[i]);
        reader = new InputStreamReader(stream, encodingName);
        scanner = new Eofclose(reader);
        scanner.yylex();
      }
      catch (IOException e) {
        System.out.println("IO error scanning file \""+argv[i]+"\"");
        System.out.println(e);
        return;
      }
      try {
      	reader.read();
      	System.out.println("Reader still open.");
      }
      catch (IOException e) {
      	System.out.println("Reader closed. Exception is: "+e);
      }      
    }
  }
%}

%%

.+  { /* ignore */ }
\n  { /* that too */ }
