%%

%unicode 6.3
%public
%class UnicodePropList_Soft_Dotted_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Soft_Dotted} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
