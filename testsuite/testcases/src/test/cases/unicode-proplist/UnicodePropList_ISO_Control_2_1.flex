%%

%unicode 2.1
%public
%class UnicodePropList_ISO_Control_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ISO Control} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
