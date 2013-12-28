%%

%unicode 2.0
%public
%class UnicodeAge_2_0_age_subtraction

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
[\p{Age:2.0}--\p{Age:1.1}] { setCurCharPropertyValue("[\\p{Age:2.0}--\\p{Age:1.1}]"); }
[^] { }
