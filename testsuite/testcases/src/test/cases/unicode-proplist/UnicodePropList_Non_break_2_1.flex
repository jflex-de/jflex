%%

%unicode 2.1
%public
%class UnicodePropList_Non_break_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Non-break} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
