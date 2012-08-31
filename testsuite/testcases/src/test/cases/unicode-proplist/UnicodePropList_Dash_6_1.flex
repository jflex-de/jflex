%%

%unicode 6.1
%public
%class UnicodePropList_Dash_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Dash} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
