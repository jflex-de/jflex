%%

%unicode 5.1
%public
%class UnicodePropList_Other_Alphabetic_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Other_Alphabetic} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
