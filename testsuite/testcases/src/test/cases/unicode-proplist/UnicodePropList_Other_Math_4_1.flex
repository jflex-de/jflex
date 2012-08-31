%%

%unicode 4.1
%public
%class UnicodePropList_Other_Math_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Other_Math} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
