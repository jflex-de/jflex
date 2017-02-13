%%

%unicode 9.0
%public
%class UnicodeWord2_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\w { setCurCharPropertyValue("Word"); }
[^\w] { setCurCharPropertyValue("Not-Word"); }
