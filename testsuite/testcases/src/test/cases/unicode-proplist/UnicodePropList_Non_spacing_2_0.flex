%%

%unicode 2.0
%public
%class UnicodePropList_Non_spacing_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Non-spacing} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
