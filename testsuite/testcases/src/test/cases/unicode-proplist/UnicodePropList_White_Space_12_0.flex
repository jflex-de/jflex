%%

%unicode 12.0
%public
%class UnicodePropList_White_Space_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
