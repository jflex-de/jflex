%%

%unicode 2.0
%public
%class UnicodePropList_Private_Use_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Private Use} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
