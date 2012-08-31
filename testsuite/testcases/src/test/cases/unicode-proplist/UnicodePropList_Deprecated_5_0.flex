%%

%unicode 5.0
%public
%class UnicodePropList_Deprecated_5_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Deprecated} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
