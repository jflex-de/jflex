%%

%unicode 12.0
%public
%class UnicodePropList_Ideographic_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Ideographic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
