%%

%unicode 9.0
%public
%class UnicodeWordBreak_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{WordBreak:ALetter} { setCurCharPropertyValue("WordBreak:ALetter"); }
\p{WordBreak:CR} { setCurCharPropertyValue("WordBreak:CR"); }
\p{WordBreak:Double_Quote} { setCurCharPropertyValue("WordBreak:Double_Quote"); }
\p{WordBreak:E_Base} { setCurCharPropertyValue("WordBreak:E_Base"); }
\p{WordBreak:E_Base_GAZ} { setCurCharPropertyValue("WordBreak:E_Base_GAZ"); }
\p{WordBreak:E_Modifier} { setCurCharPropertyValue("WordBreak:E_Modifier"); }
\p{WordBreak:Extend} { setCurCharPropertyValue("WordBreak:Extend"); }
\p{WordBreak:ExtendNumLet} { setCurCharPropertyValue("WordBreak:ExtendNumLet"); }
\p{WordBreak:Format} { setCurCharPropertyValue("WordBreak:Format"); }
\p{WordBreak:Glue_After_Zwj} { setCurCharPropertyValue("WordBreak:Glue_After_Zwj"); }
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
\p{WordBreak:ZWJ} { setCurCharPropertyValue("WordBreak:ZWJ"); }
