%%

%unicode 6.3
%public
%class UnicodePropList_Ideographic_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Ideographic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
