%%

%unicode 4.0
%public
%class UnicodePropList_Hyphen_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hyphen} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
