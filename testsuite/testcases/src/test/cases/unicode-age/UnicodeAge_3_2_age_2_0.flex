%%

%unicode 3.2
%public
%class UnicodeAge_3_2_age_2_0

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:2.0} { setCurCharPropertyValue("Age:2.0"); }
[^] { }
