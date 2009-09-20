%%

%unicode 3.2
%public
%class UnicodePropList_Logical_Order_Exception_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Logical_Order_Exception} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
