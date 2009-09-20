%%

%unicode 5.1
%public
%class UnicodePropList_Deprecated_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Deprecated} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
