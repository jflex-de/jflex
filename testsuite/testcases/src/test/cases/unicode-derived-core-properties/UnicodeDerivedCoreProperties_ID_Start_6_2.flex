%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_ID_Start_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
