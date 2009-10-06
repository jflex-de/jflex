%%

%unicode 4.0
%public
%class UnicodeWord2_4_0

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\w { setCurCharPropertyValue("Word"); }
[^\w] { setCurCharPropertyValue("Not-Word"); }
