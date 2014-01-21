%%

%unicode 6.0
%public
%class UnicodeAge_6_0_age_1_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:1.1} { setCurCharPropertyValue("Age:1.1"); }
[^] { }
