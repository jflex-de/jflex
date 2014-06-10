%%

%unicode 5.2
%public
%class UnicodePropList_Deprecated_5_2

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Deprecated} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
