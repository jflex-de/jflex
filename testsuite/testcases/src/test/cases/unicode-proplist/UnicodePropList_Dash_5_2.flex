%%

%unicode 5.2
%public
%class UnicodePropList_Dash_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Dash} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
