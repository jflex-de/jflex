%%

%unicode 5.1
%public
%class UnicodePropList_Hyphen_5_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Hyphen} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
