%%

%unicode 12.1
%public
%class UnicodePropList_Soft_Dotted_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Soft_Dotted} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
