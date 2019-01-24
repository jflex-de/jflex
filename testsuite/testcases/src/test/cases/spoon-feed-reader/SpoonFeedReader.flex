import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

%%

%unicode
%public
%class SpoonFeedReader
%type String

%{
  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java SpoonFeedReader [ --encoding <name> ] <inputfile>");
      return;
    } 
    int filePos = 0;
    String encodingName = "UTF-8";
    if (argv[0].equals("--encoding")) {
      filePos = 2;
      encodingName = argv[1];
      try {
        java.nio.charset.Charset.forName(encodingName); // Side-effect: is encodingName valid? 
      } catch (Exception e) {
        System.out.println("Invalid encoding '" + encodingName + "'");
        return;
      }
    }
    
    try (FileInputStream stream = new FileInputStream(argv[filePos]);
         Reader fileReader = new InputStreamReader(stream, encodingName)) {
      int ch;
      StringBuilder builder = new StringBuilder();
      // FileInputStream+InputStreamReader will not split surrogate chars,
      // so we stuff the input into a String and instead use a StringReader,
      // which allows us to read in a single high surrogate char 
      // without its paired low surrogate char.
      while ((ch = fileReader.read()) != -1) {
        builder.append((char)ch);
      }
      int maxChars = 9; // Hard-coded to split surrogate characters in the input "12345678𐌀"
      try (StringReader stringReader = new StringReader(builder.toString());
           Reader spoonFeedingReader = new SpoonFeedMaxCharsReaderWrapper(maxChars, stringReader)) {
        SpoonFeedReader scanner = new SpoonFeedReader(spoonFeedingReader);
        String str = scanner.yylex();
      }
    } catch (java.io.FileNotFoundException e) {
      System.out.println("File not found : \"" + argv[filePos] + "\"");
    } catch (java.io.IOException e) {
      System.out.println("IO error scanning file \"" + argv[filePos] + "\"");
      System.out.println(e);
    } catch (Exception e) {
      System.out.println("Unexpected exception:");
      e.printStackTrace(System.out);
    }
  }
%}

%%
// Recognize any sequence of code points; exclude surrogate chars
[^\uD800-\uDFFF]+ { System.out.println(yytext()); return yytext(); }

[^] { System.out.printf("char: \\u%04X", yytext().charAt(0)); return null; }  
