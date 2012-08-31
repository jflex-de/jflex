%%

%unicode 3.2
%public
%class UnicodePropList_Diacritic_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Diacritic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
