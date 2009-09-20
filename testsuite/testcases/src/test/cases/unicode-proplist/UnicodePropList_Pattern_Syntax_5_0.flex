%%

%unicode 5.0
%public
%class UnicodePropList_Pattern_Syntax_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Pattern_Syntax} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
