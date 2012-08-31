%%

%unicode 4.1
%public
%class UnicodeDerivedCoreProperties_Alphabetic_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
