%%

%unicode 9.0
%public
%class UnicodeAge_9_0_age_3_2

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:3.2} { setCurCharPropertyValue("Age:3.2"); }
[^] { }
