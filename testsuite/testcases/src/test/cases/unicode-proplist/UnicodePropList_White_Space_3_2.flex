%%

%unicode 3.2
%public
%class UnicodePropList_White_Space_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{White_Space} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
