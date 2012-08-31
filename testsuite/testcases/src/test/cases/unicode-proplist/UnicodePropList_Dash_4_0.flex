%%

%unicode 4.0
%public
%class UnicodePropList_Dash_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Dash} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
