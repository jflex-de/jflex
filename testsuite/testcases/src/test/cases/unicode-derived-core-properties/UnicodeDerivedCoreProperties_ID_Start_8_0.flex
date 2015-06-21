%%

%unicode 8.0
%public
%class UnicodeDerivedCoreProperties_ID_Start_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{ID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
