%%

%unicode 3.0
%public
%class UnicodePropList_Non_break_3_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Non-break} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
