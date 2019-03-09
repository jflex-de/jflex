%%

%unicode 12.0
%public
%class UnicodePropList_Logical_Order_Exception_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Logical_Order_Exception} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
