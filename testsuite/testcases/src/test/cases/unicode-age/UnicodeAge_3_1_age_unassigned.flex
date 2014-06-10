%%

%unicode 3.1
%public
%class UnicodeAge_3_1_age_unassigned

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Age:Unassigned} { setCurCharPropertyValue("Age:Unassigned"); }
[^] { }
