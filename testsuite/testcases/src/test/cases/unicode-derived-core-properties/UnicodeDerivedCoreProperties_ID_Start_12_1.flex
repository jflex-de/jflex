%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_ID_Start_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{ID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
