%%

%unicode 4.1
%public
%class UnicodePropList_Other_ID_Continue_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_ID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
