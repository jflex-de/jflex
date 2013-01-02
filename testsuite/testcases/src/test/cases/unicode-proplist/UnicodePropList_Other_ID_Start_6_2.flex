%%

%unicode 6.2
%public
%class UnicodePropList_Other_ID_Start_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_ID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
