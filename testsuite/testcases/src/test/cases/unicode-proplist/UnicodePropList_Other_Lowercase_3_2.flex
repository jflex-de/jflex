%%

%unicode 3.2
%public
%class UnicodePropList_Other_Lowercase_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Lowercase} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
