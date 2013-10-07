%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_ID_Start_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
