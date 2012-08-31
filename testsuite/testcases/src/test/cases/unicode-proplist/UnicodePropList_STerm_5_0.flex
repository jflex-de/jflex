%%

%unicode 5.0
%public
%class UnicodePropList_STerm_5_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{STerm} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
