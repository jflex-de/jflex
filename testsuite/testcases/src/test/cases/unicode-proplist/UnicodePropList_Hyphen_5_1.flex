%%

%unicode 5.1
%public
%class UnicodePropList_Hyphen_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hyphen} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
