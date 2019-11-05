%%

%unicode 10.0
%public
%class UnicodeDerivedCoreProperties_ID_Continue_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{ID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
