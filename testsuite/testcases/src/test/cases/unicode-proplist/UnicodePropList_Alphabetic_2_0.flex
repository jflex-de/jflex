%%

%unicode 2.0
%public
%class UnicodePropList_Alphabetic_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
