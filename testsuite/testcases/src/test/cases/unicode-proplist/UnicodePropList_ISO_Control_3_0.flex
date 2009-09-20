%%

%unicode 3.0
%public
%class UnicodePropList_ISO_Control_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ISO Control} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
