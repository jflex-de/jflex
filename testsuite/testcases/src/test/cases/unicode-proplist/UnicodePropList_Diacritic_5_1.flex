%%

%unicode 5.1
%public
%class UnicodePropList_Diacritic_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Diacritic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
