%%

%unicode 9.0
%public
%class UnicodeAge_9_0_age_6_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:6.1} { setCurCharPropertyValue("Age:6.1"); }
[^] { }
