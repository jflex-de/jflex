%%

%unicode 8.0
%public
%class UnicodePropList_Soft_Dotted_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Soft_Dotted} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
