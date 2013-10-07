%%

%unicode 6.3
%public
%class UnicodeWord1_6_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\W { setCurCharPropertyValue("Not-Word"); }
[^\W] { setCurCharPropertyValue("Word"); }
