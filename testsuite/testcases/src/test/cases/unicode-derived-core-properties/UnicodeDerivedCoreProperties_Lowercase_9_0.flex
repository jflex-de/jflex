%%

%unicode 9.0
%public
%class UnicodeDerivedCoreProperties_Lowercase_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
