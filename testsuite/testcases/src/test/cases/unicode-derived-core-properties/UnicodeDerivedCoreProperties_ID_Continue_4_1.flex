%%

%unicode 4.1
%public
%class UnicodeDerivedCoreProperties_ID_Continue_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
