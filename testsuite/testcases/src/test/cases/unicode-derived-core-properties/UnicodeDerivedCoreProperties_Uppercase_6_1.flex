%%

%unicode 6.1
%public
%class UnicodeDerivedCoreProperties_Uppercase_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
