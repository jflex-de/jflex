%%

%unicode 3.1
%public
%class UnicodePropList_Ideographic_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Ideographic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
