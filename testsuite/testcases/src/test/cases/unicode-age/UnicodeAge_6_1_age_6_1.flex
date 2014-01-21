%%

%unicode 6.1
%public
%class UnicodeAge_6_1_age_6_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:6.1} { setCurCharPropertyValue("Age:6.1"); }
[^] { }
