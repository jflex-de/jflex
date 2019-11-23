%%

%unicode 12.1
%public
%class UnicodePropList_Other_Uppercase_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Other_Uppercase} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
