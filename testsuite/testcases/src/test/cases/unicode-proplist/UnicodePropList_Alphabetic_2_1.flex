%%

%unicode 2.1
%public
%class UnicodePropList_Alphabetic_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Alphabetic} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
