%%

%unicode 6.3
%public
%class UnicodePropList_STerm_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{STerm} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
