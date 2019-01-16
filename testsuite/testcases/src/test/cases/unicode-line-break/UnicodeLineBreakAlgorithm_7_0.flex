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

%unicode 7.0
%public
%class UnicodeLineBreakAlgorithm_7_0
%type String

%{
  private static final String LINE_BREAK_OPPORTUNITY = "÷";
  private static final String NO_LINE_BREAK_OPPORTUNITY = "×";
  private static final String LINE_SEP = System.getProperty("line.separator");
  private static final Pattern COMMENT = Pattern.compile("\\s*#.*");
  private static final Pattern HEX_CHAR = Pattern.compile("[0-9A-Fa-f]{4,6}");

  private StringBuilder builder = new StringBuilder();

  public static void main(String argv[]) {
    if (argv.length == 0) {
      System.out.println("Usage : java UnicodeLineBreakAlgorithm_7_0 [ --encoding <name> ] <inputfile(s)>");
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
      
      UnicodeLineBreakAlgorithm_7_0 scanner = null;
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
                (Character.toChars(Integer.parseInt(hexCharMatcher.group(0), 16)));
            }
            Reader testReader = new StringReader(testStringBuilder.toString());            
            if (null == scanner) {
              scanner = new UnicodeLineBreakAlgorithm_7_0(testReader);
            } else {
              scanner.yyreset(testReader);
            }
            List<String> line = new ArrayList<String>();
            line.add(NO_LINE_BREAK_OPPORTUNITY);
            while ( ! scanner.zzAtEOF ) {
              String segment = scanner.yylex();
              if (null != segment) {
                for (int chnum = 0 ; chnum < segment.length() ; ) {
                  int ch = segment.codePointAt(chnum);
                  line.add(String.format("%04X", ch));
                  chnum += Character.charCount(ch);
                  if (chnum != segment.length()) {
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


// UAX#14 Unicode Line Breaking Algorithm
// <http://unicode.org/reports/tr14/>
//
// Resolve line breaking classes:
//
// LB1  Assign a line breaking class to each code point of the input.
// Resolve AI, CB, CJ, SA, SG, and XX into other line breaking classes
// depending on criteria outside the scope of this algorithm.
//
// In the absence of such criteria all characters with a specific combination
// of original class and General_Category property value are resolved as follows:
//
//     Resolved   Original     General_Category
//     AL         AI, SG, XX   Any
//     CM         SA           Only Mn or Mc
//     AL         SA           Any except Mn and Mc
//     NS         CJ           Any
//
// Note that with negation and union you also have (by applying DeMorgan)
// intersection and set difference: the intersection of a and b is !(!a|!b), 
// the expression that matches everything of a not matched by b is !(!a|b)
//
// (Surrogates are not resolved to AL here - \p{LB:SG} is not defined in JFlex.)
//
AL = [\p{LB:AL}\p{LB:AI}\p{LB:XX}] | [^\P{LB:SA}\p{gc:Mn}\p{gc:Mc}]
CM = [\p{LB:CM}[\p{LB:SA}--[^\p{gc:Mn}\p{gc:Mc}]]]
NS = [\p{LB:NS}\p{LB:CJ}]

%%

// Start and end of text:
//
// There are two special logical positions: sot, which occurs before the first
// character in the text, and eot, which occurs after the last character in
// the text. Thus an empty string would consist of sot followed immediately by
// eot. With these two definitions, the line break rules for start and end of
// text can be specified as follows:
//
// LB2  Never break at the start of text.
//
//    sot ×
//
// LB3  Always break at the end of text.
//
//    ! eot
//
// These two rules are designed to deal with degenerate cases, so that there
// is at least one character on each line, and at least one line break for the
// whole text. Emergency line breaking behavior usually also allows line
// breaks anywhere on the line if a legal line break cannot be found. This has
// the effect of preventing text from running into the margins.
//
<<EOF>> { return nextSegment(); }

// Mandatory breaks:
//
// A hard line break can consist of BK or a Newline Function (NLF) as described
// in Section 5.8, Newline Guidelines, of [Unicode]. These three rules are
// designed to handle the line ending and line separating characters as
// described there.
//
// LB4  Always break after hard line breaks.
//
//    BK !
//
\p{LB:BK} {CM}+ / \p{LB:GL} { return addFirstCharPushCMsAndReturnNextSegment(); }
\p{LB:BK} / [^] { addMatch(); return nextSegment(); }

// LB5  Treat CR followed by LF, as well as CR, LF, and NL as hard line breaks.
//
//    CR × LF  ;  CR !  ;  LF !  ;  NL !
//
\p{LB:CR} \p{LB:LF} { addMatch(); return nextSegment(); }
[\p{LB:CR}\p{LB:LF}\p{LB:NL}] / [^] { addMatch(); return nextSegment(); }

// LB6  Do not break before hard line breaks.
//
//    × ( BK | CR | LF | NL )
//
// Explicit breaks and non-breaks:
//
// LB7  Do not break before spaces or zero width space.
//
//    × SP  ;  × ZW
//
// LB8  Break before any character following a zero-width space,
//      even if one or more spaces intervene.
//
//    ZW SP* ÷
//
// (LB6  lookaheads included:  × ( BK | CR | LF | NL ) )
// (LB7  lookaheads included:  × ( SP | ZW ) )
//
\p{LB:ZW} {CM}+ / \p{LB:GL} { return addFirstCharPushCMsAndReturnNextSegment(); }
\p{LB:ZW} \p{LB:SP}* / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:ZW}] { addMatch(); }
\p{LB:ZW} \p{LB:SP}* / \P{LB:SP} { addMatch(); return nextSegment(); }
\p{LB:ZW} \p{LB:SP}+ { addMatch(); return nextSegment(); }

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
// CM behaves as if it had the type of its base character.  Note that despite
// the summary title, this rule is not limited to standard combining character
// sequences.  For the purposes of line breaking, sequences containing most
// of the control codes or layout control characters are treated like combining
// sequences.
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

// 6.3 Tailorable Line Breaking Rules
//
// The following rules and the classes referenced in them provide a reasonable 
// default set of line break opportunities. Implementations should implement 
// them unless alternate approaches produce better results for some classes of 
// text or applications. When using alternative rules or algorithms,
// implementations must ensure that the mandatory breaks, break opportunities
// and non-break positions determined by the algorithm and rules of Section 6.3,
// Non-tailorable Line Breaking Rules, are preserved. See Section 4, Conformance.
//
// Non-breaking characters:
//
// LB12a  Do not break before NBSP and related characters, except after spaces
// and hyphens.
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
// × CL  ;  × CP  ;  × EX  ;  × IS  ;  × SY
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
// (CL | CP) SP* × NS
//
[\p{LB:CL}\p{LB:CP}] {CM}* \p{LB:SP}* / {NS} { addMatch(); }


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
// (LB13 lookaheads included:  × ( CL | CP | EX | IS | SY ) )
//
\p{LB:SP} / {CM}+ [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:CP}\p{LB:EX}\p{LB:IS}\p{LB:SY}\p{LB:QU}\p{LB:BA}\p{LB:HY}] { addMatch();  return nextSegment(); }
\p{LB:SP} / {CM}+ {NS} { addMatch();  return nextSegment(); }
\p{LB:SP} {CM}+ / \p{LB:GL} { return addFirstCharPushCMsAndReturnNextSegment(); }
\p{LB:SP} / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:CP}\p{LB:EX}\p{LB:IS}\p{LB:SY}] { addMatch(); }
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
// (LB13 lookaheads included:  × ( CL | CP | EX | IS | SY ) )
// (LB19 lookahead  included:  × QU )
//
\p{LB:CB} {CM}* / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:CP}\p{LB:EX}\p{LB:IS}\p{LB:SY}\p{LB:QU}] { addMatch(); }
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

// LB21a  Don't break after Hebrew + Hyphen
//
// HL (HY | BA) ×
//
// (LB20 lookahead included)
//
\p{LB:HL} {CM}* [\p{LB:HY}\p{LB:BA}] {CM}* / \p{LB:CB} { addMatch(); return nextSegment(); }
\p{LB:HL} {CM}* [\p{LB:HY}\p{LB:BA}] {CM}* / [^] { addMatch(); }
\p{LB:HL} {CM}* [\p{LB:HY}\p{LB:BA}] {CM}* { addMatch(); }

// LB22  Do not break between two ellipses, or between letters or numbers and ellipsis.
//
// (AL | HL) × IN  ;  ID × IN  ;  IN × IN  ;  NU × IN
//
// Examples: ‘9...’, ‘a...’, ‘H...’
//
(({AL} | [\p{LB:HL}\p{LB:ID}\p{LB:IN}\p{LB:NU}]) {CM}* | {CM}+) / \p{LB:IN} { addMatch(); }

// Numbers:
//
// Do not break alphanumerics.
// 
// LB23  Do not break within ‘a9’, ‘3a’, or ‘H%’.
//
// ID × PO  ;  (AL | HL) × NU  ;  NU × (AL | HL)
//
\p{LB:ID} {CM}* / \p{LB:PO} { addMatch(); }
(({AL} | \p{LB:HL}) {CM}* | {CM}+) / \p{LB:NU} { addMatch(); }
\p{LB:NU} {CM}* / ({AL} | \p{LB:HL}) { addMatch(); }


// LB24  Do not break between prefix and letters or ideographs.
//
// PR × ID  ;  PR × (AL | HL)  ;  PO × (AL | HL)
//
\p{LB:PR} {CM}* / ({AL} | [\p{LB:ID}\p{LB:HL}]) { addMatch(); }
\p{LB:PO} {CM}* / ({AL} | \p{LB:HL}) { addMatch(); }


// In general, it is recommended to not break lines inside numbers of the form
// described by the following regular expression:
//
// ( PR | PO ) ? ( OP | HY ) ? NU (NU | SY | IS) * (CL | CP) ? ( PR | PO ) ?
//
// Examples:  $(12.35)    2,1234    (12)¢    12.54¢
//
// [The following directly implement the above, so the LB25 approximation is not needed.]
//
([\p{LB:PR}\p{LB:PO}] {CM}*)? [\p{LB:OP}\p{LB:HY}] {CM}* / \p{LB:NU} { addMatch(); }
[\p{LB:PR}\p{LB:PO}] {CM}* / \p{LB:NU} { addMatch(); }
\p{LB:NU} {CM}* ([\p{LB:NU}\p{LB:SY}\p{LB:IS}] {CM}*)* [\p{LB:CL}\p{LB:CP}]? / [\p{LB:PR}\p{LB:PO}] { addMatch(); }
\p{LB:NU} {CM}* [\p{LB:NU}\p{LB:SY}\p{LB:IS}]* / [\p{LB:CL}\p{LB:CP}] { addMatch(); }
\p{LB:NU} {CM}* / [\p{LB:NU}\p{LB:SY}\p{LB:IS}] { addMatch(); }



// Korean syllable blocks
//
// Conjoining jamos, Hangul syllables, or combinations of both form Korean 
// Syllable Blocks. Such blocks are effectively treated as if they were Hangul 
// syllables; no breaks can occur in the middle of a syllable block. 
// See Unicode Standard Annex #29, “Unicode Text Segmentation” [UAX29], for more 
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
//      (JL | JV | JT | H2 | H3) × IN
//
//      (JL | JV | JT | H2 | H3) × PO
//
//      PR × (JL | JV | JT | H2 | H3)
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
// (AL | HL) × (AL | HL)
//
(({AL} | \p{LB:HL}) {CM}* | {CM}+) / ({AL} | \p{LB:HL}) { addMatch(); }


// LB29  Do not break between numeric punctuation and alphabetics (“e.g.”).
//
// IS × (AL | HL)
//
\p{LB:IS} {CM}* / ({AL} | \p{LB:HL}) { addMatch(); }


// LB30 Do not break between letters, numbers, or ordinary symbols and opening 
// or closing parentheses.
//
// (AL | HL | NU) × OP
//
// CP × (AL | HL | NU)
//
// The purpose of this rule is to prevent breaks in common cases where a part 
// of a word appears between delimiters--for example, in “person(s)”.
//
({AL} | [\p{LB:HL}\p{LB:NU}]) {CM}* / \p{LB:OP} { addMatch(); }
\p{LB:CP} {CM}* / ({AL} | [\p{LB:HL}\p{LB:NU}]) { addMatch(); }


// LB30a  Do not break between regional indicator symbols.
//
//    RI × RI
//
\p{LB:RI} {CM}* / \p{LB:RI} { addMatch(); }


// Although the rule below is not specified, without it, at least one test string fails:
// ÷ 000B ÷ 0308 × 0028 ÷	#  ÷ [0.2] <LINE TABULATION> (BK) ÷ [4.0] COMBINING DIAERESIS (CM) × [30.01] LEFT PARENTHESIS (OP) ÷ [0.3]
// ( Rule 30.01 is: (AL|NU)×OP ) 
{CM}+ / \p{LB:OP} { addMatch(); }


// LB31  Break everywhere else.
//
// ALL ÷  ;  ÷ ALL
//
// (LB6  lookaheads included:  × ( BK | CR | LF | NL ) )
// (LB7  lookaheads included:  × ( SP | ZW ) )
// (LB11 lookahead  included:  × WJ )
// (LB13 lookaheads included:  × ( CL | CP | EX | IS | SY ) )
// (LB19 lookahead  included:  × QU )
// (LB21 lookaheads included:  × ( BA | HY | NS ) )
//
[^\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:ZW}] {CM}* / [\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:SP}\p{LB:ZW}\p{LB:WJ}\p{LB:CL}\p{LB:CP}\p{LB:EX}\p{LB:IS}\p{LB:SY}\p{LB:QU}\p{LB:BA}\p{LB:HY}] { addMatch(); }
[^\p{LB:BK}\p{LB:CR}\p{LB:LF}\p{LB:NL}\p{LB:ZW}] {CM}* / {NS} { addMatch(); }
[^] / [^] { addMatch(); return nextSegment(); }
[^] { addMatch(); }
