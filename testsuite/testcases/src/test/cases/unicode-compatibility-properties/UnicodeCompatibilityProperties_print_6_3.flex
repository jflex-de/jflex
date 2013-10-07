%%

%unicode 6.3
%public
%class UnicodeCompatibilityProperties_print_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
