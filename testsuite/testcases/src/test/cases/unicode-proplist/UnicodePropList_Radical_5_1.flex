%%

%unicode 5.1
%public
%class UnicodePropList_Radical_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Radical} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
