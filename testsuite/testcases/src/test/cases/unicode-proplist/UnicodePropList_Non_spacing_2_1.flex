%%

%unicode 2.1
%public
%class UnicodePropList_Non_spacing_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Non-spacing} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
