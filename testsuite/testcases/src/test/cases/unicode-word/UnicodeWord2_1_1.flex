%%

%unicode 1.1
%public
%class UnicodeWord2_1_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\w { setCurCharPropertyValue("Word"); }
[^\w] { setCurCharPropertyValue("Not-Word"); }
