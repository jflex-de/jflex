%%

%unicode 6.3
%public
%class UnicodePropList_Diacritic_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Diacritic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
