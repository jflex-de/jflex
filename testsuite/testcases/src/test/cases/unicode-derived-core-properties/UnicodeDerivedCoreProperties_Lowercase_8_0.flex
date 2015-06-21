%%

%unicode 8.0
%public
%class UnicodeDerivedCoreProperties_Lowercase_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
