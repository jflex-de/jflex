%%

%unicode 3.0
%public
%class UnicodePropList_Combining_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Combining} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
