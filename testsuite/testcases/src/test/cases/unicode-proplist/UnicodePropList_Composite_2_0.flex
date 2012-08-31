%%

%unicode 2.0
%public
%class UnicodePropList_Composite_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Composite} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
