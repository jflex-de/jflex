%%

%unicode 9.0
%public
%class UnicodeSentenceBreak_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{SentenceBreak:ATerm} { setCurCharPropertyValue("SentenceBreak:ATerm"); }
\p{SentenceBreak:CR} { setCurCharPropertyValue("SentenceBreak:CR"); }
\p{SentenceBreak:Close} { setCurCharPropertyValue("SentenceBreak:Close"); }
\p{SentenceBreak:Extend} { setCurCharPropertyValue("SentenceBreak:Extend"); }
\p{SentenceBreak:Format} { setCurCharPropertyValue("SentenceBreak:Format"); }
\p{SentenceBreak:LF} { setCurCharPropertyValue("SentenceBreak:LF"); }
\p{SentenceBreak:Lower} { setCurCharPropertyValue("SentenceBreak:Lower"); }
\p{SentenceBreak:Numeric} { setCurCharPropertyValue("SentenceBreak:Numeric"); }
\p{SentenceBreak:OLetter} { setCurCharPropertyValue("SentenceBreak:OLetter"); }
\p{SentenceBreak:Other} { setCurCharPropertyValue("SentenceBreak:Other"); }
\p{SentenceBreak:SContinue} { setCurCharPropertyValue("SentenceBreak:SContinue"); }
\p{SentenceBreak:STerm} { setCurCharPropertyValue("SentenceBreak:STerm"); }
\p{SentenceBreak:Sep} { setCurCharPropertyValue("SentenceBreak:Sep"); }
\p{SentenceBreak:Sp} { setCurCharPropertyValue("SentenceBreak:Sp"); }
\p{SentenceBreak:Upper} { setCurCharPropertyValue("SentenceBreak:Upper"); }
