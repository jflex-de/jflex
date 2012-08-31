%%

%unicode 2.1
%public
%class UnicodePropList_Zero_width_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Zero-width} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
