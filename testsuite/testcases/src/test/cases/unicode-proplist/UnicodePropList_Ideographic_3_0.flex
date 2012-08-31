%%

%unicode 3.0
%public
%class UnicodePropList_Ideographic_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Ideographic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
