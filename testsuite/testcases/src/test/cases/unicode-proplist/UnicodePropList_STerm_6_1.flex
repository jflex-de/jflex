%%

%unicode 6.1
%public
%class UnicodePropList_STerm_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{STerm} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
