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

%unicode 6.3
%public
%class UnicodeWordBreakRules_6_3
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
      System.out.println("Usage : java UnicodeWordBreakRules_6_3 [ --encoding <name> ] <inputfile(s)>");
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
      
      UnicodeWordBreakRules_6_3 scanner = null;
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
              scanner = new UnicodeWordBreakRules_6_3(testReader);
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
// WB1. 	sot 	÷ 	
// WB2. 		÷ 	eot
<<EOF>> { return nextSegment(); }


// Do not break within CRLF.
//
// WB3. 	CR 	×  LF
//
\p{WB:CR} \p{WB:LF} / [^] { addMatch(); return nextSegment(); }
\p{WB:CR} \p{WB:LF} { addMatch(); return nextSegment(); }


// Otherwise break before and after Newlines (including CR and LF)
//
// WB3a. 	(Newline | CR | LF) 	÷
//
[\p{WB:Newline}\p{WB:CR}\p{WB:LF}] / [^]{2} { addMatch(); return nextSegment(); }
[\p{WB:Newline}\p{WB:CR}\p{WB:LF}] / [^] { addMatch(); return nextSegment(); }


// WB3b. 	  	÷ 	(Newline | CR | LF)
//
[^] / [\p{WB:Newline}\p{WB:CR}\p{WB:LF}] [^] { addMatch(); return nextSegment(); }
[^] / [\p{WB:Newline}\p{WB:CR}\p{WB:LF}] { addMatch(); return nextSegment(); }


// Ignore Format and Extend characters, except when they appear at the 
// beginning of a region of text.
//
// (See Section 6.3, Replacing Ignore Rules.)
//
// WB4. 	X (Extend | Format)* 	→ 	X
//
//      --> [^ Newline CR LF ] × [Format Extend]
//
[^\p{WB:Newline}\p{WB:CR}\p{WB:LF}] / [\p{WB:Format}\p{WB:Extend}] { addMatch(); }


// Do not break between most letters.
//
// WB5. 	(ALetter | Hebrew_Letter) 	× 	(ALetter | Hebrew_Letter)
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
[\p{WB:ALetter}\p{WB:Hebrew_Letter}] [\p{WB:Format}\p{WB:Extend}]* / [\p{WB:ALetter}\p{WB:Hebrew_Letter}] [^] { addMatch(); }
[\p{WB:ALetter}\p{WB:Hebrew_Letter}] [\p{WB:Format}\p{WB:Extend}]* / [\p{WB:ALetter}\p{WB:Hebrew_Letter}] { addMatch(); }


// Do not break letters across certain punctuation.
//
// WB6. 	(ALetter | Hebrew_Letter) 	× 	(MidLetter | MidNumLet | Single_Quote) (ALetter | Hebrew_Letter)
// WB7. 	(ALetter | Hebrew_Letter) (MidLetter | MidNumLet | Single_Quote) 	× 	(ALetter | Hebrew_Letter)
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
[\p{WB:ALetter}\p{WB:Hebrew_Letter}] [\p{WB:Format}\p{WB:Extend}]* [\p{WB:MidLetter}\p{WB:MidNumLet}\p{WB:Single_Quote}] [\p{WB:Format}\p{WB:Extend}]* / [\p{WB:ALetter}\p{WB:Hebrew_Letter}] { addMatch(); }


// WB7a. 	Hebrew_Letter 	× 	Single_Quote
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:Hebrew_Letter} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Single_Quote} { addMatch(); } 


// WB7b. 	Hebrew_Letter 	× 	Double_Quote Hebrew_Letter
// WB7c. 	Hebrew_Letter Double_Quote 	× 	Hebrew_Letter
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:Hebrew_Letter} [\p{WB:Format}\p{WB:Extend}]* \p{WB:Double_Quote} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Hebrew_Letter} { addMatch(); }


// Do not break within sequences of digits, or digits adjacent to letters
// (“3a”, or “A3”).
//
// WB8. 	Numeric 	× 	Numeric
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:Numeric} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Numeric} [^] { addMatch(); }
\p{WB:Numeric} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Numeric} { addMatch(); }


// WB9. 	(ALetter | Hebrew_Letter) 	× 	Numeric
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
[\p{WB:ALetter}\p{WB:Hebrew_Letter}] [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Numeric} [^] { addMatch(); }
[\p{WB:ALetter}\p{WB:Hebrew_Letter}] [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Numeric} { addMatch(); }


// WB10. 	Numeric 	× 	(ALetter | Hebrew_Letter)
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:Numeric} [\p{WB:Format}\p{WB:Extend}]* / [\p{WB:ALetter}\p{WB:Hebrew_Letter}] [^] { addMatch(); }
\p{WB:Numeric} [\p{WB:Format}\p{WB:Extend}]* / [\p{WB:ALetter}\p{WB:Hebrew_Letter}] { addMatch(); }


// Do not break within sequences, such as “3.2” or “3,456.789”.
//
// WB11. 	Numeric (MidNum | MidNumLet | Single_Quote) 	× 	Numeric
// WB12. 	Numeric 	× 	(MidNum | MidNumLet | Single_Quote) Numeric
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:Numeric} [\p{WB:Format}\p{WB:Extend}]* [\p{WB:MidNum}\p{WB:MidNumLet}\p{WB:Single_Quote}] [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Numeric} { addMatch(); }


// Do not break between Katakana.
//
// WB13. 	Katakana 	× 	Katakana
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:Katakana} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Katakana} [^] { addMatch(); }
\p{WB:Katakana} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Katakana} { addMatch(); }

// Do not break from extenders.
//
// WB13a. 	(ALetter | Hebrew_Letter | Numeric | Katakana | ExtendNumLet) 	× 	ExtendNumLet
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
[\p{WB:ALetter}\p{WB:Hebrew_Letter}\p{WB:Numeric}\p{WB:Katakana}\p{WB:ExtendNumLet}] [\p{WB:Format}\p{WB:Extend}]* / \p{WB:ExtendNumLet} [^] { addMatch(); }
[\p{WB:ALetter}\p{WB:Hebrew_Letter}\p{WB:Numeric}\p{WB:Katakana}\p{WB:ExtendNumLet}] [\p{WB:Format}\p{WB:Extend}]* / \p{WB:ExtendNumLet} { addMatch(); }


// WB13b. 	ExtendNumLet 	× 	(ALetter | Hebrew_Letter | Numeric | Katakana) 
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:ExtendNumLet} [\p{WB:Format}\p{WB:Extend}]* / [\p{WB:ALetter}\p{WB:Hebrew_Letter}\p{WB:Numeric}\p{WB:Katakana}] [^] { addMatch(); }
\p{WB:ExtendNumLet} [\p{WB:Format}\p{WB:Extend}]* / [\p{WB:ALetter}\p{WB:Hebrew_Letter}\p{WB:Numeric}\p{WB:Katakana}] { addMatch(); }


// Do not break between regional indicator symbols.
//
// WB13c. 	Regional_Indicator 	× 	Regional_Indicator
//
// [included WB4. 	X (Extend | Format)* 	→ 	X]
//
\p{WB:Regional_Indicator} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Regional_Indicator} [^] { addMatch(); }
\p{WB:Regional_Indicator} [\p{WB:Format}\p{WB:Extend}]* / \p{WB:Regional_Indicator} { addMatch(); }


// Otherwise, break everywhere (including around ideographs).
//
// WB14. 	Any 	÷ 	Any
//
[^] { addMatch(); return nextSegment(); }
