%%

%unicode 3.0
%public
%class UnicodePropList_Unassigned_Code_Value_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Unassigned Code Value} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
