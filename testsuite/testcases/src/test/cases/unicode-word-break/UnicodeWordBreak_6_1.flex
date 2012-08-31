%%

%unicode 6.1
%public
%class UnicodeWordBreak_6_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{WordBreak:ALetter} { setCurCharPropertyValue("WordBreak:ALetter"); }
\p{WordBreak:CR} { setCurCharPropertyValue("WordBreak:CR"); }
\p{WordBreak:Extend} { setCurCharPropertyValue("WordBreak:Extend"); }
\p{WordBreak:ExtendNumLet} { setCurCharPropertyValue("WordBreak:ExtendNumLet"); }
\p{WordBreak:Format} { setCurCharPropertyValue("WordBreak:Format"); }
\p{WordBreak:Katakana} { setCurCharPropertyValue("WordBreak:Katakana"); }
\p{WordBreak:LF} { setCurCharPropertyValue("WordBreak:LF"); }
\p{WordBreak:MidLetter} { setCurCharPropertyValue("WordBreak:MidLetter"); }
\p{WordBreak:MidNum} { setCurCharPropertyValue("WordBreak:MidNum"); }
\p{WordBreak:MidNumLet} { setCurCharPropertyValue("WordBreak:MidNumLet"); }
\p{WordBreak:Newline} { setCurCharPropertyValue("WordBreak:Newline"); }
\p{WordBreak:Numeric} { setCurCharPropertyValue("WordBreak:Numeric"); }
\p{WordBreak:Other} { setCurCharPropertyValue("WordBreak:Other"); }
