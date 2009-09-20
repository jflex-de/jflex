%%

%unicode 3.0
%public
%class UnicodePropList_Zero_width_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Zero-width} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
