%%

%unicode 11.0
%public
%class UnicodeDerivedCoreProperties_ID_Start_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{ID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
