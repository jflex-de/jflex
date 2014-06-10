%%

%unicode 5.0
%public
%class UnicodePropList_Dash_5_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Dash} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
