%%

%unicode 4.1
%public
%class UnicodeBlocks_4_1

%type int
%standalone

%{
  private static final int maxCodePoint = 0xFFFD;
  private final String[] charBlock = new String[maxCodePoint + 1];
  
  private void printOutput() {
    String prevBlockName = charBlock[0];
    int begCodePoint = 0;
    for (int codePoint = 1 ; codePoint <= maxCodePoint ; ++codePoint ) {
      if (codePoint == 0xD800) { // Skip the surrogate blocks
        printBlock(begCodePoint, codePoint - 1, prevBlockName);
        begCodePoint = codePoint = 0xE000;
        prevBlockName = charBlock[codePoint];
        continue;
      }
      String blockName = charBlock[codePoint];
      if (null == blockName || ! blockName.equals(prevBlockName)) {
        printBlock(begCodePoint, codePoint - 1, prevBlockName);
        prevBlockName = blockName;
        begCodePoint = codePoint;
      }
    }
    printBlock(begCodePoint, maxCodePoint, prevBlockName);
  }
  
  private void printBlock(int begCodePoint, int endCodePoint, String blockName) {
    System.out.println
      (toHex(begCodePoint) + ".." + toHex(endCodePoint) + "; " + blockName);    
  }
  
  private String toHex(int codePoint) {
    StringBuilder builder = new StringBuilder();
    String codePointStr = Integer.toString(codePoint, 16).toUpperCase();
    for (int padPos = 0 ; padPos < 4 - codePointStr.length() ; ++padPos) {
      builder.append('0');
    }
    builder.append(codePointStr);
    return builder.toString();
  }
%} 

%% 

\p{Block:Alphabetic Presentation Forms} { charBlock[ (int)yytext().charAt(0) ] = "Alphabetic Presentation Forms"; }
\p{Block:Arabic} { charBlock[ (int)yytext().charAt(0) ] = "Arabic"; }
\p{Block:Arabic Presentation Forms-A} { charBlock[ (int)yytext().charAt(0) ] = "Arabic Presentation Forms-A"; }
\p{Block:Arabic Presentation Forms-B} { charBlock[ (int)yytext().charAt(0) ] = "Arabic Presentation Forms-B"; }
\p{Block:Arabic Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Arabic Supplement"; }
\p{Block:Armenian} { charBlock[ (int)yytext().charAt(0) ] = "Armenian"; }
\p{Block:Arrows} { charBlock[ (int)yytext().charAt(0) ] = "Arrows"; }
\p{Block:Basic Latin} { charBlock[ (int)yytext().charAt(0) ] = "Basic Latin"; }
\p{Block:Bengali} { charBlock[ (int)yytext().charAt(0) ] = "Bengali"; }
\p{Block:Block Elements} { charBlock[ (int)yytext().charAt(0) ] = "Block Elements"; }
\p{Block:Bopomofo} { charBlock[ (int)yytext().charAt(0) ] = "Bopomofo"; }
\p{Block:Bopomofo Extended} { charBlock[ (int)yytext().charAt(0) ] = "Bopomofo Extended"; }
\p{Block:Box Drawing} { charBlock[ (int)yytext().charAt(0) ] = "Box Drawing"; }
\p{Block:Braille Patterns} { charBlock[ (int)yytext().charAt(0) ] = "Braille Patterns"; }
\p{Block:Buginese} { charBlock[ (int)yytext().charAt(0) ] = "Buginese"; }
\p{Block:Buhid} { charBlock[ (int)yytext().charAt(0) ] = "Buhid"; }
\p{Block:CJK Compatibility} { charBlock[ (int)yytext().charAt(0) ] = "CJK Compatibility"; }
\p{Block:CJK Compatibility Forms} { charBlock[ (int)yytext().charAt(0) ] = "CJK Compatibility Forms"; }
\p{Block:CJK Compatibility Ideographs} { charBlock[ (int)yytext().charAt(0) ] = "CJK Compatibility Ideographs"; }
\p{Block:CJK Radicals Supplement} { charBlock[ (int)yytext().charAt(0) ] = "CJK Radicals Supplement"; }
\p{Block:CJK Strokes} { charBlock[ (int)yytext().charAt(0) ] = "CJK Strokes"; }
\p{Block:CJK Symbols and Punctuation} { charBlock[ (int)yytext().charAt(0) ] = "CJK Symbols and Punctuation"; }
\p{Block:CJK Unified Ideographs} { charBlock[ (int)yytext().charAt(0) ] = "CJK Unified Ideographs"; }
\p{Block:CJK Unified Ideographs Extension A} { charBlock[ (int)yytext().charAt(0) ] = "CJK Unified Ideographs Extension A"; }
\p{Block:Cherokee} { charBlock[ (int)yytext().charAt(0) ] = "Cherokee"; }
\p{Block:Combining Diacritical Marks} { charBlock[ (int)yytext().charAt(0) ] = "Combining Diacritical Marks"; }
\p{Block:Combining Diacritical Marks Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Combining Diacritical Marks Supplement"; }
\p{Block:Combining Diacritical Marks for Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Combining Diacritical Marks for Symbols"; }
\p{Block:Combining Half Marks} { charBlock[ (int)yytext().charAt(0) ] = "Combining Half Marks"; }
\p{Block:Control Pictures} { charBlock[ (int)yytext().charAt(0) ] = "Control Pictures"; }
\p{Block:Coptic} { charBlock[ (int)yytext().charAt(0) ] = "Coptic"; }
\p{Block:Currency Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Currency Symbols"; }
\p{Block:Cyrillic} { charBlock[ (int)yytext().charAt(0) ] = "Cyrillic"; }
\p{Block:Cyrillic Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Cyrillic Supplement"; }
\p{Block:Devanagari} { charBlock[ (int)yytext().charAt(0) ] = "Devanagari"; }
\p{Block:Dingbats} { charBlock[ (int)yytext().charAt(0) ] = "Dingbats"; }
\p{Block:Enclosed Alphanumerics} { charBlock[ (int)yytext().charAt(0) ] = "Enclosed Alphanumerics"; }
\p{Block:Enclosed CJK Letters and Months} { charBlock[ (int)yytext().charAt(0) ] = "Enclosed CJK Letters and Months"; }
\p{Block:Ethiopic} { charBlock[ (int)yytext().charAt(0) ] = "Ethiopic"; }
\p{Block:Ethiopic Extended} { charBlock[ (int)yytext().charAt(0) ] = "Ethiopic Extended"; }
\p{Block:Ethiopic Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Ethiopic Supplement"; }
\p{Block:General Punctuation} { charBlock[ (int)yytext().charAt(0) ] = "General Punctuation"; }
\p{Block:Geometric Shapes} { charBlock[ (int)yytext().charAt(0) ] = "Geometric Shapes"; }
\p{Block:Georgian} { charBlock[ (int)yytext().charAt(0) ] = "Georgian"; }
\p{Block:Georgian Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Georgian Supplement"; }
\p{Block:Glagolitic} { charBlock[ (int)yytext().charAt(0) ] = "Glagolitic"; }
\p{Block:Greek Extended} { charBlock[ (int)yytext().charAt(0) ] = "Greek Extended"; }
\p{Block:Greek and Coptic} { charBlock[ (int)yytext().charAt(0) ] = "Greek and Coptic"; }
\p{Block:Gujarati} { charBlock[ (int)yytext().charAt(0) ] = "Gujarati"; }
\p{Block:Gurmukhi} { charBlock[ (int)yytext().charAt(0) ] = "Gurmukhi"; }
\p{Block:Halfwidth and Fullwidth Forms} { charBlock[ (int)yytext().charAt(0) ] = "Halfwidth and Fullwidth Forms"; }
\p{Block:Hangul Compatibility Jamo} { charBlock[ (int)yytext().charAt(0) ] = "Hangul Compatibility Jamo"; }
\p{Block:Hangul Jamo} { charBlock[ (int)yytext().charAt(0) ] = "Hangul Jamo"; }
\p{Block:Hangul Syllables} { charBlock[ (int)yytext().charAt(0) ] = "Hangul Syllables"; }
\p{Block:Hanunoo} { charBlock[ (int)yytext().charAt(0) ] = "Hanunoo"; }
\p{Block:Hebrew} { charBlock[ (int)yytext().charAt(0) ] = "Hebrew"; }
\p{Block:Hiragana} { charBlock[ (int)yytext().charAt(0) ] = "Hiragana"; }
\p{Block:IPA Extensions} { charBlock[ (int)yytext().charAt(0) ] = "IPA Extensions"; }
\p{Block:Ideographic Description Characters} { charBlock[ (int)yytext().charAt(0) ] = "Ideographic Description Characters"; }
\p{Block:Kanbun} { charBlock[ (int)yytext().charAt(0) ] = "Kanbun"; }
\p{Block:Kangxi Radicals} { charBlock[ (int)yytext().charAt(0) ] = "Kangxi Radicals"; }
\p{Block:Kannada} { charBlock[ (int)yytext().charAt(0) ] = "Kannada"; }
\p{Block:Katakana} { charBlock[ (int)yytext().charAt(0) ] = "Katakana"; }
\p{Block:Katakana Phonetic Extensions} { charBlock[ (int)yytext().charAt(0) ] = "Katakana Phonetic Extensions"; }
\p{Block:Khmer} { charBlock[ (int)yytext().charAt(0) ] = "Khmer"; }
\p{Block:Khmer Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Khmer Symbols"; }
\p{Block:Lao} { charBlock[ (int)yytext().charAt(0) ] = "Lao"; }
\p{Block:Latin Extended Additional} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended Additional"; }
\p{Block:Latin Extended-A} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended-A"; }
\p{Block:Latin Extended-B} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended-B"; }
\p{Block:Latin-1 Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Latin-1 Supplement"; }
\p{Block:Letterlike Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Letterlike Symbols"; }
\p{Block:Limbu} { charBlock[ (int)yytext().charAt(0) ] = "Limbu"; }
\p{Block:Malayalam} { charBlock[ (int)yytext().charAt(0) ] = "Malayalam"; }
\p{Block:Mathematical Operators} { charBlock[ (int)yytext().charAt(0) ] = "Mathematical Operators"; }
\p{Block:Miscellaneous Mathematical Symbols-A} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Mathematical Symbols-A"; }
\p{Block:Miscellaneous Mathematical Symbols-B} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Mathematical Symbols-B"; }
\p{Block:Miscellaneous Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Symbols"; }
\p{Block:Miscellaneous Symbols and Arrows} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Symbols and Arrows"; }
\p{Block:Miscellaneous Technical} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Technical"; }
\p{Block:Modifier Tone Letters} { charBlock[ (int)yytext().charAt(0) ] = "Modifier Tone Letters"; }
\p{Block:Mongolian} { charBlock[ (int)yytext().charAt(0) ] = "Mongolian"; }
\p{Block:Myanmar} { charBlock[ (int)yytext().charAt(0) ] = "Myanmar"; }
\p{Block:New Tai Lue} { charBlock[ (int)yytext().charAt(0) ] = "New Tai Lue"; }
\p{Block:No Block} { charBlock[ (int)yytext().charAt(0) ] = "No Block"; }
\p{Block:Number Forms} { charBlock[ (int)yytext().charAt(0) ] = "Number Forms"; }
\p{Block:Ogham} { charBlock[ (int)yytext().charAt(0) ] = "Ogham"; }
\p{Block:Optical Character Recognition} { charBlock[ (int)yytext().charAt(0) ] = "Optical Character Recognition"; }
\p{Block:Oriya} { charBlock[ (int)yytext().charAt(0) ] = "Oriya"; }
\p{Block:Phonetic Extensions} { charBlock[ (int)yytext().charAt(0) ] = "Phonetic Extensions"; }
\p{Block:Phonetic Extensions Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Phonetic Extensions Supplement"; }
\p{Block:Private Use Area} { charBlock[ (int)yytext().charAt(0) ] = "Private Use Area"; }
\p{Block:Runic} { charBlock[ (int)yytext().charAt(0) ] = "Runic"; }
\p{Block:Sinhala} { charBlock[ (int)yytext().charAt(0) ] = "Sinhala"; }
\p{Block:Small Form Variants} { charBlock[ (int)yytext().charAt(0) ] = "Small Form Variants"; }
\p{Block:Spacing Modifier Letters} { charBlock[ (int)yytext().charAt(0) ] = "Spacing Modifier Letters"; }
\p{Block:Specials} { charBlock[ (int)yytext().charAt(0) ] = "Specials"; }
\p{Block:Superscripts and Subscripts} { charBlock[ (int)yytext().charAt(0) ] = "Superscripts and Subscripts"; }
\p{Block:Supplemental Arrows-A} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Arrows-A"; }
\p{Block:Supplemental Arrows-B} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Arrows-B"; }
\p{Block:Supplemental Mathematical Operators} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Mathematical Operators"; }
\p{Block:Supplemental Punctuation} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Punctuation"; }
\p{Block:Syloti Nagri} { charBlock[ (int)yytext().charAt(0) ] = "Syloti Nagri"; }
\p{Block:Syriac} { charBlock[ (int)yytext().charAt(0) ] = "Syriac"; }
\p{Block:Tagalog} { charBlock[ (int)yytext().charAt(0) ] = "Tagalog"; }
\p{Block:Tagbanwa} { charBlock[ (int)yytext().charAt(0) ] = "Tagbanwa"; }
\p{Block:Tai Le} { charBlock[ (int)yytext().charAt(0) ] = "Tai Le"; }
\p{Block:Tamil} { charBlock[ (int)yytext().charAt(0) ] = "Tamil"; }
\p{Block:Telugu} { charBlock[ (int)yytext().charAt(0) ] = "Telugu"; }
\p{Block:Thaana} { charBlock[ (int)yytext().charAt(0) ] = "Thaana"; }
\p{Block:Thai} { charBlock[ (int)yytext().charAt(0) ] = "Thai"; }
\p{Block:Tibetan} { charBlock[ (int)yytext().charAt(0) ] = "Tibetan"; }
\p{Block:Tifinagh} { charBlock[ (int)yytext().charAt(0) ] = "Tifinagh"; }
\p{Block:Unified Canadian Aboriginal Syllabics} { charBlock[ (int)yytext().charAt(0) ] = "Unified Canadian Aboriginal Syllabics"; }
\p{Block:Variation Selectors} { charBlock[ (int)yytext().charAt(0) ] = "Variation Selectors"; }
\p{Block:Vertical Forms} { charBlock[ (int)yytext().charAt(0) ] = "Vertical Forms"; }
\p{Block:Yi Radicals} { charBlock[ (int)yytext().charAt(0) ] = "Yi Radicals"; }
\p{Block:Yi Syllables} { charBlock[ (int)yytext().charAt(0) ] = "Yi Syllables"; }
\p{Block:Yijing Hexagram Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Yijing Hexagram Symbols"; }

<<EOF>> { printOutput(); return 1; }
