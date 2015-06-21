%%

%unicode 8.0
%public
%class UnicodeDerivedCoreProperties_Case_Ignorable_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Case_Ignorable} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
