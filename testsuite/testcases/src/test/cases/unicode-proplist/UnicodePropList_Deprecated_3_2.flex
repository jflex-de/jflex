%%

%unicode 3.2
%public
%class UnicodePropList_Deprecated_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Deprecated} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
