%%

%unicode 2.0
%public
%class UnicodeBlocks_2_0

%type int
%standalone

%include src/test/cases/unicode-blocks/common-unicode-blocks-java

%% 

\p{Block:Alphabetic Presentation Forms} { setCurCharBlock("Alphabetic Presentation Forms"); }
\p{Block:Arabic} { setCurCharBlock("Arabic"); }
\p{Block:Arabic Presentation Forms-A} { setCurCharBlock("Arabic Presentation Forms-A"); }
\p{Block:Arabic Presentation Forms-B} { setCurCharBlock("Arabic Presentation Forms-B"); }
\p{Block:Armenian} { setCurCharBlock("Armenian"); }
\p{Block:Arrows} { setCurCharBlock("Arrows"); }
\p{Block:Basic Latin} { setCurCharBlock("Basic Latin"); }
\p{Block:Bengali} { setCurCharBlock("Bengali"); }
\p{Block:Block Elements} { setCurCharBlock("Block Elements"); }
\p{Block:Bopomofo} { setCurCharBlock("Bopomofo"); }
\p{Block:Box Drawing} { setCurCharBlock("Box Drawing"); }
\p{Block:CJK Compatibility} { setCurCharBlock("CJK Compatibility"); }
\p{Block:CJK Compatibility Forms} { setCurCharBlock("CJK Compatibility Forms"); }
\p{Block:CJK Compatibility Ideographs} { setCurCharBlock("CJK Compatibility Ideographs"); }
\p{Block:CJK Symbols and Punctuation} { setCurCharBlock("CJK Symbols and Punctuation"); }
\p{Block:CJK Unified Ideographs} { setCurCharBlock("CJK Unified Ideographs"); }
\p{Block:Combining Diacritical Marks} { setCurCharBlock("Combining Diacritical Marks"); }
\p{Block:Combining Half Marks} { setCurCharBlock("Combining Half Marks"); }
\p{Block:Combining Marks for Symbols} { setCurCharBlock("Combining Marks for Symbols"); }
\p{Block:Control Pictures} { setCurCharBlock("Control Pictures"); }
\p{Block:Currency Symbols} { setCurCharBlock("Currency Symbols"); }
\p{Block:Cyrillic} { setCurCharBlock("Cyrillic"); }
\p{Block:Devanagari} { setCurCharBlock("Devanagari"); }
\p{Block:Dingbats} { setCurCharBlock("Dingbats"); }
\p{Block:Enclosed Alphanumerics} { setCurCharBlock("Enclosed Alphanumerics"); }
\p{Block:Enclosed CJK Letters and Months} { setCurCharBlock("Enclosed CJK Letters and Months"); }
\p{Block:General Punctuation} { setCurCharBlock("General Punctuation"); }
\p{Block:Geometric Shapes} { setCurCharBlock("Geometric Shapes"); }
\p{Block:Georgian} { setCurCharBlock("Georgian"); }
\p{Block:Greek} { setCurCharBlock("Greek"); }
\p{Block:Greek Extended} { setCurCharBlock("Greek Extended"); }
\p{Block:Gujarati} { setCurCharBlock("Gujarati"); }
\p{Block:Gurmukhi} { setCurCharBlock("Gurmukhi"); }
\p{Block:Halfwidth and Fullwidth Forms} { setCurCharBlock("Halfwidth and Fullwidth Forms"); }
\p{Block:Hangul Compatibility Jamo} { setCurCharBlock("Hangul Compatibility Jamo"); }
\p{Block:Hangul Jamo} { setCurCharBlock("Hangul Jamo"); }
\p{Block:Hangul Syllables} { setCurCharBlock("Hangul Syllables"); }
\p{Block:Hebrew} { setCurCharBlock("Hebrew"); }
\p{Block:Hiragana} { setCurCharBlock("Hiragana"); }
\p{Block:IPA Extensions} { setCurCharBlock("IPA Extensions"); }
\p{Block:Kanbun} { setCurCharBlock("Kanbun"); }
\p{Block:Kannada} { setCurCharBlock("Kannada"); }
\p{Block:Katakana} { setCurCharBlock("Katakana"); }
\p{Block:Lao} { setCurCharBlock("Lao"); }
\p{Block:Latin Extended Additional} { setCurCharBlock("Latin Extended Additional"); }
\p{Block:Latin Extended-A} { setCurCharBlock("Latin Extended-A"); }
\p{Block:Latin Extended-B} { setCurCharBlock("Latin Extended-B"); }
\p{Block:Latin-1 Supplement} { setCurCharBlock("Latin-1 Supplement"); }
\p{Block:Letterlike Symbols} { setCurCharBlock("Letterlike Symbols"); }
\p{Block:Malayalam} { setCurCharBlock("Malayalam"); }
\p{Block:Mathematical Operators} { setCurCharBlock("Mathematical Operators"); }
\p{Block:Miscellaneous Symbols} { setCurCharBlock("Miscellaneous Symbols"); }
\p{Block:Miscellaneous Technical} { setCurCharBlock("Miscellaneous Technical"); }
\p{Block:No Block} { setCurCharBlock("No Block"); }
\p{Block:Number Forms} { setCurCharBlock("Number Forms"); }
\p{Block:Optical Character Recognition} { setCurCharBlock("Optical Character Recognition"); }
\p{Block:Oriya} { setCurCharBlock("Oriya"); }
\p{Block:Private Use} { setCurCharBlock("Private Use"); }
\p{Block:Small Form Variants} { setCurCharBlock("Small Form Variants"); }
\p{Block:Spacing Modifier Letters} { setCurCharBlock("Spacing Modifier Letters"); }
\p{Block:Specials} { setCurCharBlock("Specials"); }
\p{Block:Superscripts and Subscripts} { setCurCharBlock("Superscripts and Subscripts"); }
\p{Block:Tamil} { setCurCharBlock("Tamil"); }
\p{Block:Telugu} { setCurCharBlock("Telugu"); }
\p{Block:Thai} { setCurCharBlock("Thai"); }
\p{Block:Tibetan} { setCurCharBlock("Tibetan"); }

<<EOF>> { printOutput(); return 1; }
