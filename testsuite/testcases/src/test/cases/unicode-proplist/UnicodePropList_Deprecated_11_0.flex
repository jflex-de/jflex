%%

%unicode 11.0
%public
%class UnicodePropList_Deprecated_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Deprecated} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
