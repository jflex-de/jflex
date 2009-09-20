%%

%unicode 3.0
%public
%class UnicodePropList_Diacritic_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Diacritic} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
