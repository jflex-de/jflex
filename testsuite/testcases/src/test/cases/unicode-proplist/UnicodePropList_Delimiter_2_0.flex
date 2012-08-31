%%

%unicode 2.0
%public
%class UnicodePropList_Delimiter_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Delimiter} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
