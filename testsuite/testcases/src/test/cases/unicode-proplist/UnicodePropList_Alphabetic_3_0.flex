%%

%unicode 3.0
%public
%class UnicodePropList_Alphabetic_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
