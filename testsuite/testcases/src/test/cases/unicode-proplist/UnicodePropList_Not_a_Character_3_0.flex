%%

%unicode 3.0
%public
%class UnicodePropList_Not_a_Character_3_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Not a Character} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
