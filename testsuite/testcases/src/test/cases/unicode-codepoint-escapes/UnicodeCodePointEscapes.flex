%%

%unicode
%public
%class UnicodeCodePointEscapes

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\u{1} { setCurCharPropertyValue("matched"); }
\u{000010} { setCurCharPropertyValue("matched"); }
\u{ CFF
    D00 } { setCurCharPropertyValue("matched"); }
\u{FFFF 10000 10001} { setCurCharPropertyValue("matched"); } 

"\u{2}" { setCurCharPropertyValue("matched"); }
"\u{000011}" { setCurCharPropertyValue("matched"); }
"\u{ CFF D00 }" { setCurCharPropertyValue("matched"); }
"\u{FFF 1000 1001}" { setCurCharPropertyValue("matched"); } 

[\u{3}\u{10FFFF}] { setCurCharPropertyValue("matched"); }

[^] { setCurCharPropertyValue("inverse matched"); }