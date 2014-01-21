%%

%unicode 6.1
%public
%class UnicodeAge_6_1_age_3_2

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:3.2} { setCurCharPropertyValue("Age:3.2"); }
[^] { }
