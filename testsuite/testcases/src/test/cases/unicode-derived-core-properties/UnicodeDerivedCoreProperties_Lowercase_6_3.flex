%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_Lowercase_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
