%%

%unicode 12.0
%public
%class UnicodeDerivedCoreProperties_ID_Continue_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{ID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
