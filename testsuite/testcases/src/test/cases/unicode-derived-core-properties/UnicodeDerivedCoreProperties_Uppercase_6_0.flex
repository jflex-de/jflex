%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_Uppercase_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
