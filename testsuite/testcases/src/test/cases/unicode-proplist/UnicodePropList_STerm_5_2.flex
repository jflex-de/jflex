%%

%unicode 5.2
%public
%class UnicodePropList_STerm_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{STerm} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
