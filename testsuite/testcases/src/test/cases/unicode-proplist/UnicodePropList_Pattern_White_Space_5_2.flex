%%

%unicode 5.2
%public
%class UnicodePropList_Pattern_White_Space_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Pattern_White_Space} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
