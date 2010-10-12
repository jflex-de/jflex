%%

%unicode 6.0
%public
%class UnicodePropList_White_Space_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
