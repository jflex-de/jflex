%%

%unicode 4.0
%public
%class UnicodePropList_STerm_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{STerm} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
