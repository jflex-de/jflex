%%

%unicode 12.1
%public
%class UnicodePropList_Pattern_Syntax_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Pattern_Syntax} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
