%%

%unicode 4.0
%public
%class UnicodePropList_Ideographic_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Ideographic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
