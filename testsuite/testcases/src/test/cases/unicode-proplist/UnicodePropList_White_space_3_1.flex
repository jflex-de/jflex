%%

%unicode 3.1
%public
%class UnicodePropList_White_space_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{White_space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
