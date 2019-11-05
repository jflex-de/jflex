%%

%unicode 12.0
%public
%class UnicodeWordBreak_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{WordBreak:ALetter} { setCurCharPropertyValue("WordBreak:ALetter"); }
\p{WordBreak:CR} { setCurCharPropertyValue("WordBreak:CR"); }
\p{WordBreak:Double_Quote} { setCurCharPropertyValue("WordBreak:Double_Quote"); }
\p{WordBreak:Extend} { setCurCharPropertyValue("WordBreak:Extend"); }
\p{WordBreak:ExtendNumLet} { setCurCharPropertyValue("WordBreak:ExtendNumLet"); }
\p{WordBreak:Format} { setCurCharPropertyValue("WordBreak:Format"); }
\p{WordBreak:Hebrew_Letter} { setCurCharPropertyValue("WordBreak:Hebrew_Letter"); }
\p{WordBreak:Katakana} { setCurCharPropertyValue("WordBreak:Katakana"); }
\p{WordBreak:LF} { setCurCharPropertyValue("WordBreak:LF"); }
\p{WordBreak:MidLetter} { setCurCharPropertyValue("WordBreak:MidLetter"); }
\p{WordBreak:MidNum} { setCurCharPropertyValue("WordBreak:MidNum"); }
\p{WordBreak:MidNumLet} { setCurCharPropertyValue("WordBreak:MidNumLet"); }
\p{WordBreak:Newline} { setCurCharPropertyValue("WordBreak:Newline"); }
\p{WordBreak:Numeric} { setCurCharPropertyValue("WordBreak:Numeric"); }
\p{WordBreak:Other} { setCurCharPropertyValue("WordBreak:Other"); }
\p{WordBreak:Regional_Indicator} { setCurCharPropertyValue("WordBreak:Regional_Indicator"); }
\p{WordBreak:Single_Quote} { setCurCharPropertyValue("WordBreak:Single_Quote"); }
\p{WordBreak:WSegSpace} { setCurCharPropertyValue("WordBreak:WSegSpace"); }
\p{WordBreak:ZWJ} { setCurCharPropertyValue("WordBreak:ZWJ"); }
