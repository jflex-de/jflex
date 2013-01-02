%%

%unicode 6.2
%public
%class UnicodeCompatibilityProperties_print_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
