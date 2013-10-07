%%

%unicode 6.3
%public
%class UnicodePropList_Hyphen_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Hyphen} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
