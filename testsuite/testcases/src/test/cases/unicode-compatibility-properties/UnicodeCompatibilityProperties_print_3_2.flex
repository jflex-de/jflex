%%

%unicode 3.2
%public
%class UnicodeCompatibilityProperties_print_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
