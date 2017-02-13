%%

%unicode 9.0
%public
%class UnicodePropList_White_Space_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
