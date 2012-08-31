%%

%unicode 5.0
%public
%class UnicodePropList_Extender_5_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Extender} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
