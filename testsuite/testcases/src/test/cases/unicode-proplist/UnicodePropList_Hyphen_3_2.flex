%%

%unicode 3.2
%public
%class UnicodePropList_Hyphen_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Hyphen} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
