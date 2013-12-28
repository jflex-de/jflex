%%

%unicode 6.2
%public
%class UnicodeAge_6_2_age_6_2

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:6.2} { setCurCharPropertyValue("Age:6.2"); }
[^] { }
