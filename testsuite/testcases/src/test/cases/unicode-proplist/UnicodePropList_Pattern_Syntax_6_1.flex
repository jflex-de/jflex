%%

%unicode 6.1
%public
%class UnicodePropList_Pattern_Syntax_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Pattern_Syntax} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
