%%

%unicode 2.0
%public
%class UnicodePropList_Zero_width_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Zero-width} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
