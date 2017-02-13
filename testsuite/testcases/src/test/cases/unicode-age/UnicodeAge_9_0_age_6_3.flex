%%

%unicode 9.0
%public
%class UnicodeAge_9_0_age_6_3

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:6.3} { setCurCharPropertyValue("Age:6.3"); }
[^] { }
