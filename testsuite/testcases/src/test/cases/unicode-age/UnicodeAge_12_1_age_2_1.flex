%%

%unicode 12.1
%public
%class UnicodeAge_12_1_age_2_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:2.1} { setCurCharPropertyValue("Age:2.1"); }
[^] { }
