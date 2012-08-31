%%

%unicode 5.2
%public
%class UnicodeDerivedCoreProperties_Uppercase_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
