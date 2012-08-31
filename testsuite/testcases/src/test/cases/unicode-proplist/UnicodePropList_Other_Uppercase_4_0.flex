%%

%unicode 4.0
%public
%class UnicodePropList_Other_Uppercase_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
