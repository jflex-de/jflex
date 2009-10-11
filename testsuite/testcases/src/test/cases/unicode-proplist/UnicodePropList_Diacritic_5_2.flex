%%

%unicode 5.2
%public
%class UnicodePropList_Diacritic_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Diacritic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
