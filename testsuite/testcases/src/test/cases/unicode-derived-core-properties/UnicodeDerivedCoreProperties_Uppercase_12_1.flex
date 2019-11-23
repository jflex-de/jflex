%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_Uppercase_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
