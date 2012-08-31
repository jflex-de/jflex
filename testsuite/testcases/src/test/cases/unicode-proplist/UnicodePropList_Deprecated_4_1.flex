%%

%unicode 4.1
%public
%class UnicodePropList_Deprecated_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Deprecated} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
