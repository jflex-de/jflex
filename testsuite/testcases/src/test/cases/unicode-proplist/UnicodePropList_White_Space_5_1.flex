%%

%unicode 5.1
%public
%class UnicodePropList_White_Space_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{White_Space} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
