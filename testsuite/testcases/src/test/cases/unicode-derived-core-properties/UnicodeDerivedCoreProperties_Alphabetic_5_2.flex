%%

%unicode 5.2
%public
%class UnicodeDerivedCoreProperties_Alphabetic_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
