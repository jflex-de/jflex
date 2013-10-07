%%

%unicode 6.3
%public
%class UnicodePropList_Deprecated_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Deprecated} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
