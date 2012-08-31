%%

%unicode 4.0
%public
%class UnicodePropList_White_Space_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
