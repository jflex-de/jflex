%%

%unicode 6.3
%public
%class UnicodeAge_6_3_age_3_2

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:3.2} { setCurCharPropertyValue("Age:3.2"); }
[^] { }
