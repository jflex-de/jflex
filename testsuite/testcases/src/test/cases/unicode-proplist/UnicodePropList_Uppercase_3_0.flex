%%

%unicode 3.0
%public
%class UnicodePropList_Uppercase_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
