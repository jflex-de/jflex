%%

%unicode 8.0
%public
%class UnicodePropList_Hyphen_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Hyphen} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
