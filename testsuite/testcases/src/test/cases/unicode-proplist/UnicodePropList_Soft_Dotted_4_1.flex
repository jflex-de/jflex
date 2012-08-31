%%

%unicode 4.1
%public
%class UnicodePropList_Soft_Dotted_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Soft_Dotted} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
