%%

%unicode 11.0
%public
%class UnicodeAge_11_0_age_4_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:4.1} { setCurCharPropertyValue("Age:4.1"); }
[^] { }
