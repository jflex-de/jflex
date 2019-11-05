%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_Cased_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Cased} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
