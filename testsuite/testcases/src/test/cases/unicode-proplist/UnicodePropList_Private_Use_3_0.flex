%%

%unicode 3.0
%public
%class UnicodePropList_Private_Use_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Private Use} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
