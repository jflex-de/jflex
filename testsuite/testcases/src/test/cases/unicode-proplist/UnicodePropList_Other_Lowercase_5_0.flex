%%

%unicode 5.0
%public
%class UnicodePropList_Other_Lowercase_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Lowercase} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
