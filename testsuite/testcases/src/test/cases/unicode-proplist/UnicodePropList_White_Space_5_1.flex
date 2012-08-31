%%

%unicode 5.1
%public
%class UnicodePropList_White_Space_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
