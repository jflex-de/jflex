%%

%unicode 3.0
%public
%class UnicodePropList_Zero_width_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Zero-width} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
