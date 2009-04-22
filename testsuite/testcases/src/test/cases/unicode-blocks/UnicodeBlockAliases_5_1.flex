%%

%unicode 5.1
%public
%class UnicodeBlockAliases_5_1

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

\p{blk=Alphabetic Presentation Forms} { charBlock[ (int)yytext().charAt(0) ] = "Alphabetic Presentation Forms"; }
\p{blk=Arabic} { charBlock[ (int)yytext().charAt(0) ] = "Arabic"; }
\p{blk=Arabic Presentation Forms-A} { charBlock[ (int)yytext().charAt(0) ] = "Arabic Presentation Forms-A"; }
\p{blk=Arabic Presentation Forms-B} { charBlock[ (int)yytext().charAt(0) ] = "Arabic Presentation Forms-B"; }
\p{blk=Arabic Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Arabic Supplement"; }
\p{blk=Armenian} { charBlock[ (int)yytext().charAt(0) ] = "Armenian"; }
\p{blk=Arrows} { charBlock[ (int)yytext().charAt(0) ] = "Arrows"; }
\p{blk=Balinese} { charBlock[ (int)yytext().charAt(0) ] = "Balinese"; }
\p{blk=Basic Latin} { charBlock[ (int)yytext().charAt(0) ] = "Basic Latin"; }
\p{blk=Bengali} { charBlock[ (int)yytext().charAt(0) ] = "Bengali"; }
\p{blk=Block Elements} { charBlock[ (int)yytext().charAt(0) ] = "Block Elements"; }
\p{blk=Bopomofo} { charBlock[ (int)yytext().charAt(0) ] = "Bopomofo"; }
\p{blk=Bopomofo Extended} { charBlock[ (int)yytext().charAt(0) ] = "Bopomofo Extended"; }
\p{blk=Box Drawing} { charBlock[ (int)yytext().charAt(0) ] = "Box Drawing"; }
\p{blk=Braille Patterns} { charBlock[ (int)yytext().charAt(0) ] = "Braille Patterns"; }
\p{blk=Buginese} { charBlock[ (int)yytext().charAt(0) ] = "Buginese"; }
\p{blk=Buhid} { charBlock[ (int)yytext().charAt(0) ] = "Buhid"; }
\p{blk=CJK Compatibility} { charBlock[ (int)yytext().charAt(0) ] = "CJK Compatibility"; }
\p{blk=CJK Compatibility Forms} { charBlock[ (int)yytext().charAt(0) ] = "CJK Compatibility Forms"; }
\p{blk=CJK Compatibility Ideographs} { charBlock[ (int)yytext().charAt(0) ] = "CJK Compatibility Ideographs"; }
\p{blk=CJK Radicals Supplement} { charBlock[ (int)yytext().charAt(0) ] = "CJK Radicals Supplement"; }
\p{blk=CJK Strokes} { charBlock[ (int)yytext().charAt(0) ] = "CJK Strokes"; }
\p{blk=CJK Symbols and Punctuation} { charBlock[ (int)yytext().charAt(0) ] = "CJK Symbols and Punctuation"; }
\p{blk=CJK Unified Ideographs} { charBlock[ (int)yytext().charAt(0) ] = "CJK Unified Ideographs"; }
\p{blk=CJK Unified Ideographs Extension A} { charBlock[ (int)yytext().charAt(0) ] = "CJK Unified Ideographs Extension A"; }
\p{blk=Cham} { charBlock[ (int)yytext().charAt(0) ] = "Cham"; }
\p{blk=Cherokee} { charBlock[ (int)yytext().charAt(0) ] = "Cherokee"; }
\p{blk=Combining Diacritical Marks} { charBlock[ (int)yytext().charAt(0) ] = "Combining Diacritical Marks"; }
\p{blk=Combining Diacritical Marks Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Combining Diacritical Marks Supplement"; }
\p{blk=Combining Diacritical Marks for Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Combining Diacritical Marks for Symbols"; }
\p{blk=Combining Half Marks} { charBlock[ (int)yytext().charAt(0) ] = "Combining Half Marks"; }
\p{blk=Control Pictures} { charBlock[ (int)yytext().charAt(0) ] = "Control Pictures"; }
\p{blk=Coptic} { charBlock[ (int)yytext().charAt(0) ] = "Coptic"; }
\p{blk=Currency Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Currency Symbols"; }
\p{blk=Cyrillic} { charBlock[ (int)yytext().charAt(0) ] = "Cyrillic"; }
\p{blk=Cyrillic Extended-A} { charBlock[ (int)yytext().charAt(0) ] = "Cyrillic Extended-A"; }
\p{blk=Cyrillic Extended-B} { charBlock[ (int)yytext().charAt(0) ] = "Cyrillic Extended-B"; }
\p{blk=Cyrillic Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Cyrillic Supplement"; }
\p{blk=Devanagari} { charBlock[ (int)yytext().charAt(0) ] = "Devanagari"; }
\p{blk=Dingbats} { charBlock[ (int)yytext().charAt(0) ] = "Dingbats"; }
\p{blk=Enclosed Alphanumerics} { charBlock[ (int)yytext().charAt(0) ] = "Enclosed Alphanumerics"; }
\p{blk=Enclosed CJK Letters and Months} { charBlock[ (int)yytext().charAt(0) ] = "Enclosed CJK Letters and Months"; }
\p{blk=Ethiopic} { charBlock[ (int)yytext().charAt(0) ] = "Ethiopic"; }
\p{blk=Ethiopic Extended} { charBlock[ (int)yytext().charAt(0) ] = "Ethiopic Extended"; }
\p{blk=Ethiopic Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Ethiopic Supplement"; }
\p{blk=General Punctuation} { charBlock[ (int)yytext().charAt(0) ] = "General Punctuation"; }
\p{blk=Geometric Shapes} { charBlock[ (int)yytext().charAt(0) ] = "Geometric Shapes"; }
\p{blk=Georgian} { charBlock[ (int)yytext().charAt(0) ] = "Georgian"; }
\p{blk=Georgian Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Georgian Supplement"; }
\p{blk=Glagolitic} { charBlock[ (int)yytext().charAt(0) ] = "Glagolitic"; }
\p{blk=Greek Extended} { charBlock[ (int)yytext().charAt(0) ] = "Greek Extended"; }
\p{blk=Greek and Coptic} { charBlock[ (int)yytext().charAt(0) ] = "Greek and Coptic"; }
\p{blk=Gujarati} { charBlock[ (int)yytext().charAt(0) ] = "Gujarati"; }
\p{blk=Gurmukhi} { charBlock[ (int)yytext().charAt(0) ] = "Gurmukhi"; }
\p{blk=Halfwidth and Fullwidth Forms} { charBlock[ (int)yytext().charAt(0) ] = "Halfwidth and Fullwidth Forms"; }
\p{blk=Hangul Compatibility Jamo} { charBlock[ (int)yytext().charAt(0) ] = "Hangul Compatibility Jamo"; }
\p{blk=Hangul Jamo} { charBlock[ (int)yytext().charAt(0) ] = "Hangul Jamo"; }
\p{blk=Hangul Syllables} { charBlock[ (int)yytext().charAt(0) ] = "Hangul Syllables"; }
\p{blk=Hanunoo} { charBlock[ (int)yytext().charAt(0) ] = "Hanunoo"; }
\p{blk=Hebrew} { charBlock[ (int)yytext().charAt(0) ] = "Hebrew"; }
\p{blk=Hiragana} { charBlock[ (int)yytext().charAt(0) ] = "Hiragana"; }
\p{blk=IPA Extensions} { charBlock[ (int)yytext().charAt(0) ] = "IPA Extensions"; }
\p{blk=Ideographic Description Characters} { charBlock[ (int)yytext().charAt(0) ] = "Ideographic Description Characters"; }
\p{blk=Kanbun} { charBlock[ (int)yytext().charAt(0) ] = "Kanbun"; }
\p{blk=Kangxi Radicals} { charBlock[ (int)yytext().charAt(0) ] = "Kangxi Radicals"; }
\p{blk=Kannada} { charBlock[ (int)yytext().charAt(0) ] = "Kannada"; }
\p{blk=Katakana} { charBlock[ (int)yytext().charAt(0) ] = "Katakana"; }
\p{blk=Katakana Phonetic Extensions} { charBlock[ (int)yytext().charAt(0) ] = "Katakana Phonetic Extensions"; }
\p{blk=Kayah Li} { charBlock[ (int)yytext().charAt(0) ] = "Kayah Li"; }
\p{blk=Khmer} { charBlock[ (int)yytext().charAt(0) ] = "Khmer"; }
\p{blk=Khmer Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Khmer Symbols"; }
\p{blk=Lao} { charBlock[ (int)yytext().charAt(0) ] = "Lao"; }
\p{blk=Latin Extended Additional} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended Additional"; }
\p{blk=Latin Extended-A} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended-A"; }
\p{blk=Latin Extended-B} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended-B"; }
\p{blk=Latin Extended-C} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended-C"; }
\p{blk=Latin Extended-D} { charBlock[ (int)yytext().charAt(0) ] = "Latin Extended-D"; }
\p{blk=Latin-1 Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Latin-1 Supplement"; }
\p{blk=Lepcha} { charBlock[ (int)yytext().charAt(0) ] = "Lepcha"; }
\p{blk=Letterlike Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Letterlike Symbols"; }
\p{blk=Limbu} { charBlock[ (int)yytext().charAt(0) ] = "Limbu"; }
\p{blk=Malayalam} { charBlock[ (int)yytext().charAt(0) ] = "Malayalam"; }
\p{blk=Mathematical Operators} { charBlock[ (int)yytext().charAt(0) ] = "Mathematical Operators"; }
\p{blk=Miscellaneous Mathematical Symbols-A} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Mathematical Symbols-A"; }
\p{blk=Miscellaneous Mathematical Symbols-B} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Mathematical Symbols-B"; }
\p{blk=Miscellaneous Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Symbols"; }
\p{blk=Miscellaneous Symbols and Arrows} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Symbols and Arrows"; }
\p{blk=Miscellaneous Technical} { charBlock[ (int)yytext().charAt(0) ] = "Miscellaneous Technical"; }
\p{blk=Modifier Tone Letters} { charBlock[ (int)yytext().charAt(0) ] = "Modifier Tone Letters"; }
\p{blk=Mongolian} { charBlock[ (int)yytext().charAt(0) ] = "Mongolian"; }
\p{blk=Myanmar} { charBlock[ (int)yytext().charAt(0) ] = "Myanmar"; }
\p{blk=NKo} { charBlock[ (int)yytext().charAt(0) ] = "NKo"; }
\p{blk=New Tai Lue} { charBlock[ (int)yytext().charAt(0) ] = "New Tai Lue"; }
\p{blk=No Block} { charBlock[ (int)yytext().charAt(0) ] = "No Block"; }
\p{blk=Number Forms} { charBlock[ (int)yytext().charAt(0) ] = "Number Forms"; }
\p{blk=Ogham} { charBlock[ (int)yytext().charAt(0) ] = "Ogham"; }
\p{blk=Ol Chiki} { charBlock[ (int)yytext().charAt(0) ] = "Ol Chiki"; }
\p{blk=Optical Character Recognition} { charBlock[ (int)yytext().charAt(0) ] = "Optical Character Recognition"; }
\p{blk=Oriya} { charBlock[ (int)yytext().charAt(0) ] = "Oriya"; }
\p{blk=Phags-pa} { charBlock[ (int)yytext().charAt(0) ] = "Phags-pa"; }
\p{blk=Phonetic Extensions} { charBlock[ (int)yytext().charAt(0) ] = "Phonetic Extensions"; }
\p{blk=Phonetic Extensions Supplement} { charBlock[ (int)yytext().charAt(0) ] = "Phonetic Extensions Supplement"; }
\p{blk=Private Use Area} { charBlock[ (int)yytext().charAt(0) ] = "Private Use Area"; }
\p{blk=Rejang} { charBlock[ (int)yytext().charAt(0) ] = "Rejang"; }
\p{blk=Runic} { charBlock[ (int)yytext().charAt(0) ] = "Runic"; }
\p{blk=Saurashtra} { charBlock[ (int)yytext().charAt(0) ] = "Saurashtra"; }
\p{blk=Sinhala} { charBlock[ (int)yytext().charAt(0) ] = "Sinhala"; }
\p{blk=Small Form Variants} { charBlock[ (int)yytext().charAt(0) ] = "Small Form Variants"; }
\p{blk=Spacing Modifier Letters} { charBlock[ (int)yytext().charAt(0) ] = "Spacing Modifier Letters"; }
\p{blk=Specials} { charBlock[ (int)yytext().charAt(0) ] = "Specials"; }
\p{blk=Sundanese} { charBlock[ (int)yytext().charAt(0) ] = "Sundanese"; }
\p{blk=Superscripts and Subscripts} { charBlock[ (int)yytext().charAt(0) ] = "Superscripts and Subscripts"; }
\p{blk=Supplemental Arrows-A} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Arrows-A"; }
\p{blk=Supplemental Arrows-B} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Arrows-B"; }
\p{blk=Supplemental Mathematical Operators} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Mathematical Operators"; }
\p{blk=Supplemental Punctuation} { charBlock[ (int)yytext().charAt(0) ] = "Supplemental Punctuation"; }
\p{blk=Syloti Nagri} { charBlock[ (int)yytext().charAt(0) ] = "Syloti Nagri"; }
\p{blk=Syriac} { charBlock[ (int)yytext().charAt(0) ] = "Syriac"; }
\p{blk=Tagalog} { charBlock[ (int)yytext().charAt(0) ] = "Tagalog"; }
\p{blk=Tagbanwa} { charBlock[ (int)yytext().charAt(0) ] = "Tagbanwa"; }
\p{blk=Tai Le} { charBlock[ (int)yytext().charAt(0) ] = "Tai Le"; }
\p{blk=Tamil} { charBlock[ (int)yytext().charAt(0) ] = "Tamil"; }
\p{blk=Telugu} { charBlock[ (int)yytext().charAt(0) ] = "Telugu"; }
\p{blk=Thaana} { charBlock[ (int)yytext().charAt(0) ] = "Thaana"; }
\p{blk=Thai} { charBlock[ (int)yytext().charAt(0) ] = "Thai"; }
\p{blk=Tibetan} { charBlock[ (int)yytext().charAt(0) ] = "Tibetan"; }
\p{blk=Tifinagh} { charBlock[ (int)yytext().charAt(0) ] = "Tifinagh"; }
\p{blk=Unified Canadian Aboriginal Syllabics} { charBlock[ (int)yytext().charAt(0) ] = "Unified Canadian Aboriginal Syllabics"; }
\p{blk=Vai} { charBlock[ (int)yytext().charAt(0) ] = "Vai"; }
\p{blk=Variation Selectors} { charBlock[ (int)yytext().charAt(0) ] = "Variation Selectors"; }
\p{blk=Vertical Forms} { charBlock[ (int)yytext().charAt(0) ] = "Vertical Forms"; }
\p{blk=Yi Radicals} { charBlock[ (int)yytext().charAt(0) ] = "Yi Radicals"; }
\p{blk=Yi Syllables} { charBlock[ (int)yytext().charAt(0) ] = "Yi Syllables"; }
\p{blk=Yijing Hexagram Symbols} { charBlock[ (int)yytext().charAt(0) ] = "Yijing Hexagram Symbols"; }
<<EOF>> { printOutput(); return 1; }
