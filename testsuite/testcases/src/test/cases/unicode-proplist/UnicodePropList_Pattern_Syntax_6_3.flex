%%

%unicode 6.3
%public
%class UnicodePropList_Pattern_Syntax_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Pattern_Syntax} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
