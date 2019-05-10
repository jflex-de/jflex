%%

%unicode 12.1
%public
%class UnicodePropList_Deprecated_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Deprecated} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
