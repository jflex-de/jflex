%%

%unicode 6.0
%public
%class UnicodePropList_Extender_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Extender} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
