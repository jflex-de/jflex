%%

%unicode 5.1
%public
%class UnicodePropList_Logical_Order_Exception_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Logical_Order_Exception} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
