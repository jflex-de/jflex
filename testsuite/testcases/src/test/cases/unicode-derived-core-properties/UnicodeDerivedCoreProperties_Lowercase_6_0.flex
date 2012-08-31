%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Lowercase_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
