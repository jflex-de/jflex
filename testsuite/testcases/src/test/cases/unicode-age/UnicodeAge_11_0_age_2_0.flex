%%

%unicode 11.0
%public
%class UnicodeAge_11_0_age_2_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:2.0} { setCurCharPropertyValue("Age:2.0"); }
[^] { }
