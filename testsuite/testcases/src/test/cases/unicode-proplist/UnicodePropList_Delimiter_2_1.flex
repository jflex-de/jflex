%%

%unicode 2.1
%public
%class UnicodePropList_Delimiter_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Delimiter} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
