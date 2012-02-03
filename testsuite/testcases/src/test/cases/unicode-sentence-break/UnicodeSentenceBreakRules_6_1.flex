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

%unicode 6.1
%public
%class UnicodeSentenceBreakRules_6_1
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
      System.out.println("Usage : java UnicodeSentenceBreakRules_6_1 [ --encoding <name> ] <inputfile(s)>");
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
      
      UnicodeSentenceBreakRules_6_1 scanner = null;
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
              scanner = new UnicodeSentenceBreakRules_6_1(testReader);
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
// SB1. 	sot 	÷ 	
// SB2. 		÷ 	eot
<<EOF>> { return nextSegment(); }


// Do not break within CRLF.
//
// SB3. 	CR 	× 	LF
//
\p{SB:CR} \p{SB:LF} { addMatch(); return nextSegment(); }


// Break after paragraph separators.
//
// SB4. 	Sep | CR | LF 	÷ 	 
//
[\p{SB:Sep}\p{SB:CR}\p{SB:LF}] / [^] { addMatch(); return nextSegment(); }


// Ignore Format and Extend characters, except when they appear at the
// beginning of a region of text. (See Section 6.2, Replacing Ignore Rules.)
//
// SB5. 	X (Extend | Format)* 	→ 	X
//
//     --> [^ Sep CR LF ] × [Format Extend]
//
[^\p{SB:Sep}\p{SB:CR}\p{SB:LF}] / [\p{SB:Extend}\p{SB:Format}] { addMatch(); }


// Do not break after ambiguous terminators like period, if they are 
// immediately followed by a number or lowercase letter, if they are between 
// uppercase letters, if the first following letter (optionally after certain 
// punctuation) is lowercase, or if they are followed by “continuation”
// punctuation such as comma, colon, or semicolon. For example, a period may be
// an abbreviation or numeric period, and thus may not mark the end of a
// sentence.
//
// SB6. 	ATerm 	× 	Numeric
//
// [included SB5. 	X (Extend | Format)* 	→ 	X]
//
\p{SB:ATerm} [\p{SB:Extend}\p{SB:Format}]* / \p{SB:Numeric} { addMatch(); }


// SB7. 	Upper ATerm 	× 	Upper
//
// [included SB5. 	X (Extend | Format)* 	→ 	X]
//
\p{SB:Upper} [\p{SB:Extend}\p{SB:Format}]* \p{SB:Aterm} [\p{SB:Extend}\p{SB:Format}]* / \p{SB:Upper} { addMatch(); }


// SB8. 	ATerm Close* Sp* 	× 	( ¬(OLetter | Upper | Lower | Sep | CR | LF | STerm | ATerm) )* Lower
//
// [included SB5. 	X (Extend | Format)* 	→ 	X]
//
\p{SB:Aterm} [\p{SB:Extend}\p{SB:Format}]* (\p{SB:Close} [\p{SB:Extend}\p{SB:Format}]*)* (\p{SB:Sp} [\p{SB:Extend}\p{SB:Format}]*)* / ([^\p{SB:OLetter}\p{SB:Upper}\p{SB:Lower}\p{SB:Sep}\p{SB:CR}\p{SB:LF}\p{SB:STerm}\p{SB:ATerm}] [\p{SB:Extend}\p{SB:Format}]*)* \p{SB:Lower} { addMatch(); } 


// SB8a. 	(STerm | ATerm) Close* Sp* 	× 	(SContinue | STerm | ATerm)
//
// [included SB5. 	X (Extend | Format)* 	→ 	X]
//
[\p{SB:STerm}\p{SB:Aterm}] [\p{SB:Extend}\p{SB:Format}]* (\p{SB:Close} [\p{SB:Extend}\p{SB:Format}]*)* (\p{SB:Sp} [\p{SB:Extend}\p{SB:Format}]*)* / [\p{SB:SContinue}\p{SB:STerm}\p{SB:Aterm}] { addMatch(); }


// Break after sentence terminators, but include closing punctuation, trailing 
// spaces, and a paragraph separator (if present).  Rules SB9-11 are designed
// to allow breaks after sequences of the following form, but not within them:
//
//     ( STerm | ATerm ) Close* Sp* ( Sep | CR | LF )?
//
// SB9. 	( STerm | ATerm ) Close* 	× 	( Close | Sp | Sep | CR | LF )
// SB10. 	( STerm | ATerm ) Close* Sp* 	× 	( Sp | Sep | CR | LF )
// SB11. 	( STerm | ATerm ) Close* Sp* ( Sep | CR | LF )? 	÷
//
// [included SB5. 	X (Extend | Format)* 	→ 	X]
//
[\p{SB:STerm}\p{SB:ATerm}] [\p{SB:Extend}\p{SB:Format}]* (\p{SB:Close} [\p{SB:Extend}\p{SB:Format}]*)* (\p{SB:Sp} [\p{SB:Extend}\p{SB:Format}]*)* [\p{SB:Sep}\p{SB:CR}\p{SB:LF}]? { addMatch(); return nextSegment(); }
[\p{SB:STerm}\p{SB:ATerm}] [\p{SB:Extend}\p{SB:Format}]* / [^\p{SB:Close}\p{SB:Sp}\p{SB:Sep}\p{SB:CR}\p{SB:LF}] { addMatch(); return nextSegment(); }

// Otherwise, do not break.
//
// SB12. 	Any 	× 	Any
//
[^] { addMatch(); }
