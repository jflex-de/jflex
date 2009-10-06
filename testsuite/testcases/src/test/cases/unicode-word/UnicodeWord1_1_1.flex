%%

%unicode 1.1
%public
%class UnicodeWord1_1_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\W { setCurCharPropertyValue("Not-Word"); }
[^\W] { setCurCharPropertyValue("Word"); }
