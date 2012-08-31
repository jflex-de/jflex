%%

%unicode 5.2
%public
%class UnicodePropList_Pattern_Syntax_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Pattern_Syntax} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
