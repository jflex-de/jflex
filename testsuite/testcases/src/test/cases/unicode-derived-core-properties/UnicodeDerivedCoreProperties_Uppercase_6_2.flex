%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_Uppercase_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
