%%

%unicode 2.0
%public
%class UnicodePropList_Unassigned_Code_Value_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Unassigned Code Value} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
