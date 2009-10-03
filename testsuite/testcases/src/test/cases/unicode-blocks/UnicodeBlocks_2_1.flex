%%

%unicode 2.1
%public
%class UnicodeBlocks_2_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%% 

\p{Block:Alphabetic Presentation Forms} { setCurCharPropertyValue("Alphabetic Presentation Forms"); }
\p{Block:Arabic} { setCurCharPropertyValue("Arabic"); }
\p{Block:Arabic Presentation Forms-A} { setCurCharPropertyValue("Arabic Presentation Forms-A"); }
\p{Block:Arabic Presentation Forms-B} { setCurCharPropertyValue("Arabic Presentation Forms-B"); }
\p{Block:Armenian} { setCurCharPropertyValue("Armenian"); }
\p{Block:Arrows} { setCurCharPropertyValue("Arrows"); }
\p{Block:Basic Latin} { setCurCharPropertyValue("Basic Latin"); }
\p{Block:Bengali} { setCurCharPropertyValue("Bengali"); }
\p{Block:Block Elements} { setCurCharPropertyValue("Block Elements"); }
\p{Block:Bopomofo} { setCurCharPropertyValue("Bopomofo"); }
\p{Block:Box Drawing} { setCurCharPropertyValue("Box Drawing"); }
\p{Block:CJK Compatibility} { setCurCharPropertyValue("CJK Compatibility"); }
\p{Block:CJK Compatibility Forms} { setCurCharPropertyValue("CJK Compatibility Forms"); }
\p{Block:CJK Compatibility Ideographs} { setCurCharPropertyValue("CJK Compatibility Ideographs"); }
\p{Block:CJK Symbols and Punctuation} { setCurCharPropertyValue("CJK Symbols and Punctuation"); }
\p{Block:CJK Unified Ideographs} { setCurCharPropertyValue("CJK Unified Ideographs"); }
\p{Block:Combining Diacritical Marks} { setCurCharPropertyValue("Combining Diacritical Marks"); }
\p{Block:Combining Half Marks} { setCurCharPropertyValue("Combining Half Marks"); }
\p{Block:Combining Marks for Symbols} { setCurCharPropertyValue("Combining Marks for Symbols"); }
\p{Block:Control Pictures} { setCurCharPropertyValue("Control Pictures"); }
\p{Block:Currency Symbols} { setCurCharPropertyValue("Currency Symbols"); }
\p{Block:Cyrillic} { setCurCharPropertyValue("Cyrillic"); }
\p{Block:Devanagari} { setCurCharPropertyValue("Devanagari"); }
\p{Block:Dingbats} { setCurCharPropertyValue("Dingbats"); }
\p{Block:Enclosed Alphanumerics} { setCurCharPropertyValue("Enclosed Alphanumerics"); }
\p{Block:Enclosed CJK Letters and Months} { setCurCharPropertyValue("Enclosed CJK Letters and Months"); }
\p{Block:General Punctuation} { setCurCharPropertyValue("General Punctuation"); }
\p{Block:Geometric Shapes} { setCurCharPropertyValue("Geometric Shapes"); }
\p{Block:Georgian} { setCurCharPropertyValue("Georgian"); }
\p{Block:Greek} { setCurCharPropertyValue("Greek"); }
\p{Block:Greek Extended} { setCurCharPropertyValue("Greek Extended"); }
\p{Block:Gujarati} { setCurCharPropertyValue("Gujarati"); }
\p{Block:Gurmukhi} { setCurCharPropertyValue("Gurmukhi"); }
\p{Block:Halfwidth and Fullwidth Forms} { setCurCharPropertyValue("Halfwidth and Fullwidth Forms"); }
\p{Block:Hangul Compatibility Jamo} { setCurCharPropertyValue("Hangul Compatibility Jamo"); }
\p{Block:Hangul Jamo} { setCurCharPropertyValue("Hangul Jamo"); }
\p{Block:Hangul Syllables} { setCurCharPropertyValue("Hangul Syllables"); }
\p{Block:Hebrew} { setCurCharPropertyValue("Hebrew"); }
\p{Block:Hiragana} { setCurCharPropertyValue("Hiragana"); }
\p{Block:IPA Extensions} { setCurCharPropertyValue("IPA Extensions"); }
\p{Block:Kanbun} { setCurCharPropertyValue("Kanbun"); }
\p{Block:Kannada} { setCurCharPropertyValue("Kannada"); }
\p{Block:Katakana} { setCurCharPropertyValue("Katakana"); }
\p{Block:Lao} { setCurCharPropertyValue("Lao"); }
\p{Block:Latin Extended Additional} { setCurCharPropertyValue("Latin Extended Additional"); }
\p{Block:Latin Extended-A} { setCurCharPropertyValue("Latin Extended-A"); }
\p{Block:Latin Extended-B} { setCurCharPropertyValue("Latin Extended-B"); }
\p{Block:Latin-1 Supplement} { setCurCharPropertyValue("Latin-1 Supplement"); }
\p{Block:Letterlike Symbols} { setCurCharPropertyValue("Letterlike Symbols"); }
\p{Block:Malayalam} { setCurCharPropertyValue("Malayalam"); }
\p{Block:Mathematical Operators} { setCurCharPropertyValue("Mathematical Operators"); }
\p{Block:Miscellaneous Symbols} { setCurCharPropertyValue("Miscellaneous Symbols"); }
\p{Block:Miscellaneous Technical} { setCurCharPropertyValue("Miscellaneous Technical"); }
\p{Block:No Block} { setCurCharPropertyValue("No Block"); }
\p{Block:Number Forms} { setCurCharPropertyValue("Number Forms"); }
\p{Block:Optical Character Recognition} { setCurCharPropertyValue("Optical Character Recognition"); }
\p{Block:Oriya} { setCurCharPropertyValue("Oriya"); }
\p{Block:Private Use} { setCurCharPropertyValue("Private Use"); }
\p{Block:Small Form Variants} { setCurCharPropertyValue("Small Form Variants"); }
\p{Block:Spacing Modifier Letters} { setCurCharPropertyValue("Spacing Modifier Letters"); }
\p{Block:Specials} { setCurCharPropertyValue("Specials"); }
\p{Block:Superscripts and Subscripts} { setCurCharPropertyValue("Superscripts and Subscripts"); }
\p{Block:Tamil} { setCurCharPropertyValue("Tamil"); }
\p{Block:Telugu} { setCurCharPropertyValue("Telugu"); }
\p{Block:Thai} { setCurCharPropertyValue("Thai"); }
\p{Block:Tibetan} { setCurCharPropertyValue("Tibetan"); }

<<EOF>> { printOutput(); return 1; }
