%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_Uppercase_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
