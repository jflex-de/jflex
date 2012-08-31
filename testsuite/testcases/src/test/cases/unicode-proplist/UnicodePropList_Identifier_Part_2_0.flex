%%

%unicode 2.0
%public
%class UnicodePropList_Identifier_Part_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Identifier Part} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
