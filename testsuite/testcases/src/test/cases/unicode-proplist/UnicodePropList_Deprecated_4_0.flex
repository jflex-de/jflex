%%

%unicode 4.0
%public
%class UnicodePropList_Deprecated_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Deprecated} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
