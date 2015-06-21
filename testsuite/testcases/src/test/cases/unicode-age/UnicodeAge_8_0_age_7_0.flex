%%

%unicode 8.0
%public
%class UnicodeAge_8_0_age_7_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:7.0} { setCurCharPropertyValue("Age:7.0"); }
[^] { }
