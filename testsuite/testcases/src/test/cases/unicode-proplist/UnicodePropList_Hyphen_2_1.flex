%%

%unicode 2.1
%public
%class UnicodePropList_Hyphen_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Hyphen} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
