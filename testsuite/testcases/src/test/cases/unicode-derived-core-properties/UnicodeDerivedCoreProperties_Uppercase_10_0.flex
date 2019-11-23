%%

%unicode 10.0
%public
%class UnicodeDerivedCoreProperties_Uppercase_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
