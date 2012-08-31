%%

%unicode 5.2
%public
%class UnicodePropList_Soft_Dotted_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Soft_Dotted} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
