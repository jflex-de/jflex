%%

%unicode 3.2
%public
%class UnicodeWord2_3_2

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\w { setCurCharPropertyValue("Word"); }
[^\w] { setCurCharPropertyValue("Not-Word"); }
