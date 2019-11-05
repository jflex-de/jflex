%%

%unicode 12.1
%public
%class UnicodePropList_Other_Alphabetic_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Other_Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
