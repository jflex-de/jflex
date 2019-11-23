%%

%unicode 12.1
%public
%class UnicodePropList_White_Space_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
