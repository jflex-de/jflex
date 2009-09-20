%%

%unicode 3.0
%public
%class UnicodePropList_Non_break_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Non-break} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
