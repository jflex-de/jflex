%%

%unicode 8.0
%public
%class UnicodeAge_8_0_age_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:8.0} { setCurCharPropertyValue("Age:8.0"); }
[^] { }
