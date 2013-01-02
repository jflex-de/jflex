%%

%unicode 6.2
%public
%class UnicodePropList_Logical_Order_Exception_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Logical_Order_Exception} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
