%%

%unicode 6.0
%public
%class UnicodePropList_Logical_Order_Exception_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Logical_Order_Exception} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
