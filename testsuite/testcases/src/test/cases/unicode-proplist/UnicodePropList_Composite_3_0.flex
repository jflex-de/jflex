%%

%unicode 3.0
%public
%class UnicodePropList_Composite_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Composite} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
