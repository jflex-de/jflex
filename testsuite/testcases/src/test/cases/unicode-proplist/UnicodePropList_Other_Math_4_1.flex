%%

%unicode 4.1
%public
%class UnicodePropList_Other_Math_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Math} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
