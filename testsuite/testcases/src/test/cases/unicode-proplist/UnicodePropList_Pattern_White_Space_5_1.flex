%%

%unicode 5.1
%public
%class UnicodePropList_Pattern_White_Space_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Pattern_White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
