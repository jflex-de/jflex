%%

%unicode 6.3
%public
%class UnicodePropList_Dash_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Dash} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
