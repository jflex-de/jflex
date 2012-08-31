%%

%unicode 6.1
%public
%class UnicodePropList_Extender_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Extender} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
