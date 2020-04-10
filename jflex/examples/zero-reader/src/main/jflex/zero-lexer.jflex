import java.io.*;

%%

%public
%class ZeroLexer

%int
  

%{
  public static void main(String[] argv) {
    if (argv.length == 0) {
      System.out.println("Usage : java ZeroLexer [ --encoding <name> ] <inputfile(s)>");
    }
    else {
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
        ZeroLexer scanner = null;
        try {
          FileInputStream stream = new FileInputStream(argv[i]);
          // the usual Reader:
          Reader reader = new InputStreamReader(stream, encodingName);
          // the broken 0-returning Reader:
          reader = new FunkyReader(reader);
          
          try {
            scanner = new ZeroLexer(reader);
            do {
              System.out.println(scanner.yylex());
              System.out.println("--"+scanner.yytext()+"--");
            } while (!scanner.zzAtEOF);
          }
          catch (IOException e) {
            System.out.println("Expected IO exception");
          }

          reader.close();
          
          stream = new FileInputStream(argv[i]);
          reader = new InputStreamReader(stream, encodingName);
          reader = new FunkyReader(reader);
          // now the wrapper for broken Readers:
          reader = new ZeroReader(reader);
          
          scanner = new ZeroLexer(reader);
          do {
            System.out.println(scanner.yylex());
            System.out.println("--"+scanner.yytext()+"--");
          } while (!scanner.zzAtEOF);

        }
        catch (java.io.FileNotFoundException e) {
          System.out.println("File not found : \""+argv[i]+"\"");
        }
        catch (java.io.IOException e) {
          System.out.println("IO error scanning file \""+argv[i]+"\"");
          System.out.println(e);
        }
        catch (Exception e) {
          System.out.println("Unexpected exception:");
          e.printStackTrace();
        }
      }
    }
  }  
  
%}


%%

"some longer fixed-length match"   { return 1; }
"a"+                               { return 2; }
[^]                                { return 3; }

<<EOF>>                            { return YYEOF; }
