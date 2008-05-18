
import java.io.*;

%%

%public
%class Eofclose
%int

%eofclose

%{
  public static void main(String argv[]) {
    for (int i = 0; i < argv.length; i++) {
      Eofclose scanner;
      FileReader reader = null;
      try {
      	reader = new java.io.FileReader(argv[i]);
        scanner = new Eofclose(reader);
        scanner.yylex();
      }
      catch (IOException e) {
        System.out.println("IO error scanning file \""+argv[i]+"\"");
        System.out.println(e);
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
