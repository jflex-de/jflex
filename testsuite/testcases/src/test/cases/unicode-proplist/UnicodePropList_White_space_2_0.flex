%%

%unicode 2.0
%public
%class UnicodePropList_White_space_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{White space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
