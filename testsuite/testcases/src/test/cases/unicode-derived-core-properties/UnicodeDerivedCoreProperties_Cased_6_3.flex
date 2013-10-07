%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_Cased_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Cased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
