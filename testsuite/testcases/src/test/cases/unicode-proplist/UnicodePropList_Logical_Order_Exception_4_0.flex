%%

%unicode 4.0
%public
%class UnicodePropList_Logical_Order_Exception_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Logical_Order_Exception} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
