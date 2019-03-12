import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
%%

%public
%class Over2GbInput
%line
%char
%unicode
%int

%{
  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java Over2GbInput [ --encoding <name> ] <inputfile(s)>");
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
      
      Over2GbInput scanner = null;
      InputStream stream = null;
      for (int i = firstFilePos; i < argv.length; i++) {
        try (FileInputStream fileStream = new FileInputStream(argv[i]);
             BufferedInputStream bufferedStream = new BufferedInputStream(fileStream)) {
          bufferedStream.mark(1024);
          try {
            stream = new GZIPInputStream(bufferedStream);
          } catch (final ZipException e) {
            bufferedStream.reset();
            stream = bufferedStream;
          }
          Reader reader = new InputStreamReader(stream, encodingName);
          BufferedReader bufferedReader = new BufferedReader(reader);
          scanner = new Over2GbInput(reader);
          while ( ! scanner.zzAtEOF ) scanner.yylex();
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
        } finally {
          if (stream != null) {
            try {
              stream.close();
            } catch (IOException e) {
              // ignore
            }
          }
        }
      }
    }
  }
%}

%% 

^.+ "\n" { if (yychar > (long)Integer.MAX_VALUE) System.out.print(yytext()); }
