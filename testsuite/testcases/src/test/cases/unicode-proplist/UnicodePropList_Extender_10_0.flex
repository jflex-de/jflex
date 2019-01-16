%%

%unicode 10.0
%public
%class UnicodePropList_Extender_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Extender} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
