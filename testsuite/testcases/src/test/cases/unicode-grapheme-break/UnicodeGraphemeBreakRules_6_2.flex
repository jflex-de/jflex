import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

%%

%unicode 6.2
%public
%class UnicodeGraphemeBreakRules_6_2
%type String

%{
  private static final String BREAK_OPPORTUNITY = "÷";
  private static final String NO_BREAK_OPPORTUNITY = "×";
  private static final String LINE_SEP = System.getProperty("line.separator");
  private static final Pattern COMMENT = Pattern.compile("\\s*#.*");
  private static final Pattern HEX_CHAR = Pattern.compile("[0-9A-Fa-f]{4}");

  private StringBuilder builder = new StringBuilder();

  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java UnicodeGraphemeBreakRules_6_2 [ --encoding <name> ] <inputfile(s)>");
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
      
      UnicodeGraphemeBreakRules_6_2 scanner = null;
      for (int i = firstFilePos; i < argv.length; i++) {
        try {
          FileInputStream stream = new FileInputStream(argv[i]);
          Reader reader = new InputStreamReader(stream, encodingName);
          BufferedReader bufferedReader = new BufferedReader(reader);
          String testLine;
          while (null != (testLine = bufferedReader.readLine())) {
            testLine = COMMENT.matcher(testLine).replaceAll("").trim();
            if (0 == testLine.length()) {
              continue;
            }
            StringBuilder testStringBuilder = new StringBuilder();
            Matcher hexCharMatcher = HEX_CHAR.matcher(testLine);
            while(hexCharMatcher.find()) {
              testStringBuilder.append
                ((char)Integer.parseInt(hexCharMatcher.group(0), 16));
            }
            Reader testReader = new StringReader(testStringBuilder.toString());            
            if (null == scanner) {
              scanner = new UnicodeGraphemeBreakRules_6_2(testReader);
            } else {
              scanner.yyreset(testReader);
            }
            List<String> line = new ArrayList<String>();
            line.add(BREAK_OPPORTUNITY);
            while ( ! scanner.zzAtEOF ) {
              String segment = scanner.yylex();
              if (null != segment) {
                for (int chnum = 0 ; chnum < segment.length() ; ++chnum) {
                  char ch = segment.charAt(chnum);
                  line.add(String.format("%04X", (int)ch));
                  if (chnum < segment.length() - 1) {
                    line.add(NO_BREAK_OPPORTUNITY);
                  } else {
                    line.add(BREAK_OPPORTUNITY);
                  }
                }
              }
            }
            boolean isFirst = true;
            for (String item : line) {
              if (isFirst) {
                isFirst = false;
              } else {
                System.out.print(' ');
              }
              System.out.print(item);
            }
            System.out.print(LINE_SEP);
          }
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
  
  /** 
   * @return The next chunk of text with no internal break opportunity,
   *  or null at end-of-input. 
   */
  String nextSegment() {
    String segment = builder.toString();
    builder.setLength(0);
    return 0 == segment.length() ? null : segment;
  }
  
  void addMatch() {
    builder.append(yytext());
  }
%}

%%

// Break at the start and end of text.
//
// GB1.  sot  ÷
// GB2.       ÷  eot
//
<<EOF>> { return nextSegment(); }


// Do not break between a CR and LF. Otherwise, break before and after controls.
//
// GB3. 	CR 	×  LF
//
\p{GCB:CR} \p{GCB:LF} { addMatch(); return nextSegment(); }


// GB4. 	( Control | CR | LF ) 	÷
//
[\p{GCB:Control}\p{GCB:CR}\p{GCB:LF}] / [^] { addMatch(); return nextSegment(); }


// GB5. 		÷ 	( Control | CR | LF )
//
[^] / [\p{GCB:Control}\p{GCB:CR}\p{GCB:LF}] { addMatch(); return nextSegment(); }


// Do not break Hangul syllable sequences.
//
// GB6. 	L 	× 	( L | V | LV | LVT )
//
\p{GCB:L} / [\p{GCB:L}\p{GCB:V}\p{GCB:LV}\p{GCB:LVT}] { addMatch(); }


// GB7. 	( LV | V ) 	× 	( V | T )
//
[\p{GCB:LV}\p{GCB:V}] / [\p{GCB:V}\p{GCB:T}] { addMatch(); }


// GB8. 	( LVT | T) 	× 	T
//
[\p{GCB:LVT}\p{GCB:T}] / \p{GCB:T} { addMatch(); }


// Do not break between regional indicator symbols.
//
// GB8a. 	Regional_Indicator 	× 	Regional_Indicator
\p{GCB:Regional_Indicator} / \p{GCB:Regional_Indicator} { addMatch(); }


// Do not break before extending characters.
//
// GB9. 	  	× 	Extend
//
[^] / \p{GCB:Extend} { addMatch(); }


// Only for extended grapheme clusters:
// Do not break before SpacingMarks, or after Prepend characters.
//
// GB9a. 	  	× 	SpacingMark
//
[^] / \p{GCB:SpacingMark} { addMatch(); }


// GB9b. 	Prepend 	× 	 
//
// Unicode 6.2 has no GCB:Prepend chars, so this is not a valid property under JFLex
//
//// \p{GCB:Prepend} / [^] { addMatch(); }


// Otherwise, break everywhere.
//
// GB10. 	Any 	÷ 	Any
//
[^] { addMatch(); return nextSegment(); }
