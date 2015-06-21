%%

%unicode 8.0
%public
%class UnicodePropList_Pattern_Syntax_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Pattern_Syntax} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
