%%

%unicode 11.0
%public
%class UnicodeDerivedCoreProperties_Case_Ignorable_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Case_Ignorable} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
