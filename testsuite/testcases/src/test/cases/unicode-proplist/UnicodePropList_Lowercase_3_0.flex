%%

%unicode 3.0
%public
%class UnicodePropList_Lowercase_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Lowercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
