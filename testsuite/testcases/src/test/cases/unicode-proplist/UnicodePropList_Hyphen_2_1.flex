%%

%unicode 2.1
%public
%class UnicodePropList_Hyphen_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Hyphen} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
