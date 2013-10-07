%%

%unicode 6.3
%public
%class UnicodePropList_White_Space_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
