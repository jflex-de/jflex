%%

%unicode 12.1
%public
%class UnicodeWord2_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\w { setCurCharPropertyValue("Word"); }
[^\w] { setCurCharPropertyValue("Not-Word"); }
