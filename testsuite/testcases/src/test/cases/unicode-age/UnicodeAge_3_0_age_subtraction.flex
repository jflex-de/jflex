%%

%unicode 3.0
%public
%class UnicodeAge_3_0_age_subtraction

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
[\p{Age:2.0}--\p{Age:1.1}] { setCurCharPropertyValue("[\\p{Age:2.0}--\\p{Age:1.1}]"); }
[\p{Age:2.1}--\p{Age:2.0}] { setCurCharPropertyValue("[\\p{Age:2.1}--\\p{Age:2.0}]"); }
[\p{Age:3.0}--\p{Age:2.1}] { setCurCharPropertyValue("[\\p{Age:3.0}--\\p{Age:2.1}]"); }
[^] { }
