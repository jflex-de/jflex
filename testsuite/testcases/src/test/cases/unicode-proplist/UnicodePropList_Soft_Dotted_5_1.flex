%%

%unicode 5.1
%public
%class UnicodePropList_Soft_Dotted_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Soft_Dotted} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
