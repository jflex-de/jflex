%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_Lowercase_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
