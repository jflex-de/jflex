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

%unicode 5.1
%public
%class LineBreak_5_1
%type String

%{
  private static final String LINE_BREAK_OPPORTUNITY = "÷";
  private static final String NO_LINE_BREAK_OPPORTUNITY = "×";
  private static final String LINE_SEP = System.getProperty("line.separator");
  private static final Pattern COMMENT = Pattern.compile("\\s*#.*");
  private static final Pattern HEX_CHAR = Pattern.compile("[0-9A-Fa-f]{4}");

  private StringBuilder builder = new StringBuilder();

  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java LineBreak_5_1 [ --encoding <name> ] <inputfile(s)>");
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
      
      LineBreak_5_1 scanner = null;
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
              scanner = new LineBreak_5_1(testReader);
            } else {
              scanner.yyreset(testReader);
            }
            List<String> line = new ArrayList<String>();
            line.add(LINE_BREAK_OPPORTUNITY);
            while ( ! scanner.zzAtEOF ) {
              String segment = scanner.yylex();
              if (null != segment) {
                for (int chnum = 0 ; chnum < segment.length() ; ++chnum) {
                  char ch = segment.charAt(chnum);
                  line.add(String.format("%04X", (int)ch));
                  if (chnum < segment.length() - 1) {
                    line.add(NO_LINE_BREAK_OPPORTUNITY);
                  } else {
                    line.add(LINE_BREAK_OPPORTUNITY);
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
   * @return The next chunk of text with no internal line break opportunity,
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
  
  String addFirstCharPushCMsAndReturnNextSegment() {
    builder.append(yycharat(0));
    String segment = builder.toString();
    builder.setLength(0);
    builder.append(yytext().substring(1));
    return 0 == segment.length() ? null : segment;
  }
%}

// Resolve line breaking classes:
//
// LB1  Assign a line breaking class to each code point of the input. 
// Resolve AI, CB, SA, SG, and XX into other line breaking classes depending 
// on criteria outside the scope of this algorithm.
// 
// In the absence of such criteria, it is recommended that classes
// AI, SA, SG, and XX be resolved to AL, except that characters of
// class SA that have General_Category Mn or Mc be resolved to CM.
// Unresolved class CB is handled in rule LB20.

// Note that with negation and union you also have (by applying DeMorgan)
// intersection and set difference: the intersection of a and b is !(!a|!b), 
// the expression that matches everything of a not matched by b is !(!a|b)
//
// (Surrogates are not resolved to AL here - \p{LB:SG} is not defined in JFlex.)
//
AL = [\p{LB:AL}\p{LB:AI}\p{LB:XX}] | [^\P{LB:SA}\p{gc:Mn}\p{gc:Mc}]
CM = \p{LB:CM} | !(!\p{LB:SA}|[^\p{gc:Mn}\p{gc:Mc}])

%%

// Start and end of text:
//
// There are two special logical positions: sot, which occurs before the 
// first character in the text, and eot, which occurs after the last character 
// in the text. Thus an empty string would consist of sot followed immediately 
// by eot. With these two definitions, the line break rules for start and end 
// of text can be specified as follows:
//
// LB2  Never break at the start of text.
//
// sot ×
//
// LB3  Always break at the end of text.
//
// ! eot
//
// These two rules are designed to deal with degenerate cases, so that there
// is at least one character on each line, and at least one line break for the 
// whole text. Emergency line breaking behavior usually also allows line breaks
// anywhere on the line if a legal line break cannot be found. This has the
// effect of preventing text from running into the margins.
//
<<EOF>> { return nextSegment(); }

// Mandatory breaks:
//
// A hard line break can consist of BK or a Newline Function (NLF) as described
// in Section 5.8, Newline Guidelines, of [Unicode5.0]. These three rules are
// designed to handle the line ending and line separating characters as
// described there.
//
// LB4  Always break after hard line breaks.
//
// BK !
//
\p{LB:BK} {CM}+ / \p{LB:GL} { return addFirstCharPushCMsAndReturnNextSegment(); }
\p{LB:BK} / [^] { addMatch(); return nextSegment(); }

// LB5  Treat CR followed by LF, as well as CR, LF, and NL as hard line breaks.
//
// CR × LF  ;  CR !  ;  LF !  ;  NL !
//
\p{LB:CR} \p{LB:LF} { addMatch(); return nextSegment(); }
[\p{LB:CR}\p{LB:LF}\p{LB:NL}] / {CM}+ \p{LB:GL} { addMatch(); return nextSegment(); }
[\p{LB:CR}\p{LB:LF}\p{LB:NL}] / [^] { addMatch(); return nextSegment(); }
 
// LB6  Do not break before hard line breaks.
//
// × ( BK | CR | LF | NL )
//
// [LB6 is handled through lookaheads on the following rules]

// Explicit breaks and non-breaks:
//
// LB7  Do not break before spaces or zero width space.
//
// × SP  ;  × ZW
//
// [LB7 is handled through lookaheads in the following rules]

// LB8  Break after zero width space.
//
// ZW ÷
//
// (LB6  lookaheads included:  × ( BK | CR | LF | NL ) )
// (LB7  lookaheads included:  × ( SP | ZW ) )
//
\p{LB:ZW} {CM}+ / \p{LB:GL} { return addFirstCharPushCMsAndReturnNextSegment(); }
\p{LB:ZW} / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}] { addMatch(); }
\p{LB:ZW} / [^] { addMatch(); return nextSegment(); }

// Combining marks:
//
// See also Section 9.2, Legacy Support for Space Character as Base for
// Combining Marks.
//
// LB9  Do not break a combining character sequence; treat it as if it has the 
// line breaking class of the base character in all of the following rules.
//
// Treat X CM* as if it were X.
//
// where X is any line break class except  BK, CR, LF, NL, SP, or ZW.
//
// At any possible break opportunity between CM and a following character, 
// CM behaves as if it had the type of its base character. Note that despite 
// the summary title of this rule it is not limited to standard combining 
// character sequences. For the purposes of line breaking, sequences containing 
// most of the control codes or layout control characters are treated like
// combining sequences.
//
// [LB9 is partially handled in the following rules.]
//
[^\p{LB:SP}\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:ZW}] {CM}+ { addMatch(); return nextSegment(); }

// LB10  Treat any remaining combining mark as AL.
//
// Treat any remaining CM as it if were AL.
//
// This catches the case where a CM is the first character on the line or
// follows SP, BK, CR, LF, NL, or ZW.
//
// [LB10 is handled in the following rules]

// Word joiner:
//
// LB11  Do not break before or after Word joiner and related characters.
//
// × WJ
//
// WJ ×
//
// [LB11 is partially handled through lookaheads in the following rules]
//
\p{LB:WJ} {CM}* / [^] { addMatch(); }
\p{LB:WJ} {CM}* { addMatch(); }


// Non-breaking characters:
//
// LB12  Do not break after NBSP and related characters.
//
// GL ×
//
\p{LB:GL} {CM}* / [^] { addMatch(); }
\p{LB:GL} {CM}* { addMatch(); }

// 6.2 Tailorable Line Breaking Rules
//
// The following rules and the classes referenced in them provide a reasonable 
// default set of line break opportunities. Implementations SHOULD implement 
// them unless alternate approaches produce better results for some classes of 
// text or applications. When using alternative rules or algorithms,
// implementations must ensure that the mandatory breaks, break opportunities
// and non-break positions determined by the algorithm and rules of Section 6.1,
// Non-tailorable Line Breaking Rules, are preserved. See Section 4, Conformance.
//
// Non-breaking characters:
//
// LB12a  Do not break before NBSP and related characters, except after spaces
// and hyphens
//
// [^SP BA HY] × GL
//  
//    Also, from LineBreakTest.html: 12.2) [^ BA HY CM] CM+ × GL
//
// The expression [^SP, BA, HY] designates any line break class other than 
// SP, BA or HY. The symbol ^ is used, instead of !, to avoid confusion with
// the use of ! to indicate an explicit break. Unlike the case for WJ,
// inserting a SP overrides the non-breaking nature of a GL. Allowing a break
// after BA or HY matches widespread implementation practice and supports a
// common way of handling special line breaking of explicit hyphens, such as
// in Polish and Portuguese. See Section 5.3, Use of Hyphen.
//
[^\p{LB:CM}\p{LB:SP}\p{LB:BA}\p{LB:HY}\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:ZW}] {CM}* / \p{LB:GL} { addMatch(); }
^ {CM}+ / \p{LB:GL} { addMatch(); }

// Opening and closing:
//
// These have special behavior with respect to spaces, and therefore come
// before rule LB18.
//
// LB13  Do not break before ‘]’ or ‘!’ or ‘;’ or ‘/’, even after spaces.
//
// × CL  ;  × EX  ;  × IS  ;  × SY
//
// [LB13 is handled through lookahead in other rules]


// LB14  Do not break after ‘[’, even after spaces.
//
// OP SP* ×
//
\p{LB:OP} {CM}* \p{LB:SP}* / [^] { addMatch(); }
\p{LB:OP} {CM}* \p{LB:SP}* { addMatch(); }


// LB15  Do not break within ‘”[’, even with intervening spaces.
//
// QU SP* × OP
//
// For more information on this rule, see the note in the description for the
// QU class.
//
\p{LB:QU} {CM}* \p{LB:SP}* / \p{LB:OP} { addMatch(); }


// LB16  Do not break between closing punctuation and a nonstarter (lb=NS), 
// even with intervening spaces.
//
// CL SP* × NS
//
\p{LB:CL} {CM}* \p{LB:SP}* / \p{LB:NS} { addMatch(); }


// LB17  Do not break within ‘——’, even with intervening spaces.
//
// B2 SP* × B2
//
\p{LB:B2} {CM}* \p{LB:SP}* / \p{LB:B2} { addMatch(); }


// Spaces:
//
// LB18  Break after spaces.
//
// SP ÷
//
// (LB6  lookaheads included:  × ( BK | CR | LF | NL ) )
// (LB7  lookaheads included:  × ( SP | ZW) )
// (LB11 lookahead  included:  × WJ )
// (LB13 lookaheads included:  × ( CL | EX | IS | SY ) )
//
\p{LB:SP} / {CM}+ [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:EX}\p{LB:IS}\p{LB:SY}\p{LB:QU}\p{LB:BA}\p{LB:HY}\p{LB:NS}] { addMatch();  return nextSegment(); }
\p{LB:SP} {CM}+ / \p{LB:GL} { return addFirstCharPushCMsAndReturnNextSegment(); }
\p{LB:SP} / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:EX}\p{LB:IS}\p{LB:SY}] { addMatch(); }
\p{LB:SP} / [^] { addMatch(); return nextSegment(); }
\p{LB:SP} { addMatch(); }


// Special case rules:
//
// LB19  Do not break before or after quotation marks, such as ‘ ” ’.
//
// × QU  ;  QU ×
//
// [LB19 is partially handled through lookaheads in following rules]
//
\p{LB:QU} {CM}* / [^] { addMatch(); }
\p{LB:QU} {CM}* { addMatch(); }


// LB20  Break before and after unresolved CB.
//
// ÷ CB  ;  CB ÷
//
// Conditional breaks should be resolved external to the line breaking rules. 
// However, the default action is to treat unresolved CB as breaking before and
// after.
//
// [LB20 is partially handled through lookaheads in following rules]
//
// (LB6  lookaheads included:  × ( BK | CR | LF | NL ) )
// (LB7  lookaheads included:  × ( SP | ZW ) )
// (LB11 lookahead  included:  × WJ )
// (LB13 lookaheads included:  × ( CL | EX | IS | SY ) )
// (LB19 lookahead  included:  × QU )
//
\p{LB:CB} {CM}* / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:EX}\p{LB:IS}\p{LB:SY}\p{LB:QU}] { addMatch(); }
\p{LB:CB} {CM}* / [^] { addMatch(); return nextSegment(); }
\p{LB:CB} {CM}* { addMatch(); return nextSegment(); }


// LB21  Do not break before hyphen-minus, other hyphens, fixed-width spaces,
// small kana, and other non-starters, or after acute accents.
//
// × BA  ;  × HY  ;  × NS  ;  BB ×
//
// [LB21 is partially handled through lookaheads in following rules]
//
// (LB20 lookahead included)
//
\p{LB:BB} {CM}* / \p{LB:CB} { addMatch(); return nextSegment(); }
\p{LB:BB} {CM}* / [^] { addMatch(); }
\p{LB:BB} {CM}* { addMatch(); }


// LB22  Do not break between two ellipses, or between letters or numbers and ellipsis.
//
// AL × IN  ;  ID × IN  ;  IN × IN  ;  NU × IN
//
(({AL} | [\p{LB:ID}\p{LB:IN}\p{LB:NU}]) {CM}* | {CM}+) / \p{LB:IN} { addMatch(); }

// Numbers:
//
// Do not break alphanumerics.
// 
// LB23  Do not break within ‘a9’, ‘3a’, or ‘H%’.
//
// ID × PO  ;  AL × NU  ;  NU × AL
//
\p{LB:ID} {CM}* / \p{LB:PO} { addMatch(); }
({AL} {CM}* | {CM}+) / \p{LB:NU} { addMatch(); }
\p{LB:NU} {CM}* / {AL} { addMatch(); }


// LB24  Do not break between prefix and letters or ideographs.
//
// PR × ID  ;  PR × AL  ;  PO × AL
//
\p{LB:PR} {CM}* / (\p{LB:ID} | {AL}) { addMatch(); }
\p{LB:PO} {CM}* / {AL} { addMatch(); }


// In general, it is recommended to not break lines inside numbers of the form
// described by the following regular expression:
//
// ( PR | PO ) ? ( OP | HY ) ? NU (NU | SY | IS) * CL ? ( PR | PO ) ?
//
// Examples:  $(12.35)    2,1234    (12)¢    12.54¢
//
([\p{LB:PR}\p{LB:PO}] {CM}*)? [\p{LB:OP}\p{LB:HY}] {CM}* / \p{LB:NU} { addMatch(); }
[\p{LB:PR}\p{LB:PO}] {CM}* / \p{LB:NU} { addMatch(); }
\p{LB:NU} {CM}* ([\p{LB:NU}\p{LB:SY}\p{LB:IS}] {CM}*)* \p{LB:CL}?  / [\p{LB:PR}\p{LB:PO}] { addMatch(); }
\p{LB:NU} {CM}* [\p{LB:NU}\p{LB:SY}\p{LB:IS}]* / \p{LB:CL} { addMatch(); }
\p{LB:NU} {CM}* / [\p{LB:NU}\p{LB:SY}\p{LB:IS}] { addMatch(); }



// Korean syllable blocks
//
// Conjoining jamo, Hangul syllables, or combinations of both form Korean 
// Syllable Blocks. Such blocks are effectively treated as if they were Hangul 
// syllables; no breaks can occur in the middle of a syllable block. 
// See Unicode Standard Annex #29, “Text Boundaries” [Boundaries], for more 
// information on Korean Syllable Blocks.
//
// LB26 Do not break a Korean syllable.
//
// JL × (JL | JV | H2 | H3)  ;  (JV | H2) × (JV | JT)  ;  (JT | H3) × JT
//
// where the notation (JT | H3) means JT or H3.
//
\p{LB:JL} {CM}* / [\p{LB:JL}\p{LB:JV}\p{LB:H2}\p{LB:H3}] { addMatch(); }
[\p{LB:JV}\p{LB:H2}] {CM}* / [\p{LB:JV}\p{LB:JT}] { addMatch(); }
[\p{LB:JT}\p{LB:H3}] {CM}* / \p{LB:JT} { addMatch(); }


// The effective line breaking class for the syllable block matches the line 
// breaking class for Hangul syllables, which is ID by default. This is
// achieved by the following rule:
//
// LB27 Treat a Korean Syllable Block the same as ID.
//
// (JL | JV | JT | H2 | H3) × IN  ;  (JL | JV | JT | H2 | H3) × PO
//
// PR × (JL | JV | JT | H2 | H3)
//
// When Korean uses SPACE for line breaking, the classes in rule LB26, as well 
// as characters of class ID, are often tailored to AL; see Section 8,
// Customization.
//
[\p{LB:JL}\p{LB:JV}\p{LB:JT}\p{LB:H2}\p{LB:H3}] {CM}* / [\p{LB:IN}\p{LB:PO}] { addMatch(); }
\p{LB:PR} {CM}* / [\p{LB:JL}\p{LB:JV}\p{LB:JT}\p{LB:H2}\p{LB:H3}] { addMatch(); }


// Finally, join alphabetic letters into words and break everything else.
//
// LB28  Do not break between alphabetics (“at”).
//
// AL × AL
//
({AL} {CM}* | {CM}+) / {AL} { addMatch(); }


// LB29  Do not break between numeric punctuation and alphabetics (“e.g.”).
//
// IS × AL
//
\p{LB:IS} {CM}* / {AL} { addMatch(); }

// LB30  Withdrawn. In Unicode 5.0, rule LB30 was intended to prevent breaks 
// in cases where a part of a word appears between delimiters—for example, 
// in “person(s)”. The rule was withdrawn because it prevented desirable breaks 
// after certain Asian punctuation characters with class CL. See Example 9 of 
// Section 8, Customization, for options for restoring the functionality.


// LB31  Break everywhere else.
//
// ALL ÷  ;  ÷ ALL
//
// (LB6  lookaheads included:  × ( BK | CR | LF | NL ) )
// (LB7  lookaheads included:  × ( SP | ZW ) )
// (LB11 lookahead  included:  × WJ )
// (LB13 lookaheads included:  × ( CL | EX | IS | SY ) )
// (LB19 lookahead  included:  × QU )
// (LB21 lookaheads included:  × ( BA | HY | NS ) )
//
[^\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:ZW}] {CM}* / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:EX}\p{LB:IS}\p{LB:SY}\p{LB:QU}\p{LB:BA}\p{LB:HY}\p{LB:NS}] { addMatch(); }
[^] / [^] { addMatch(); return nextSegment(); }
[^] { addMatch(); }
