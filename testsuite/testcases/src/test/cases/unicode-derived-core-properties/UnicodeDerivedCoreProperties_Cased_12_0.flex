%%

%unicode 12.0
%public
%class UnicodeDerivedCoreProperties_Cased_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Cased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
