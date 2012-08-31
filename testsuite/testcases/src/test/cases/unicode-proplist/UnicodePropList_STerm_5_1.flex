%%

%unicode 5.1
%public
%class UnicodePropList_STerm_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{STerm} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
