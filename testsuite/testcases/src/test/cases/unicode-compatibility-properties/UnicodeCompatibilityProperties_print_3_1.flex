%%

%unicode 3.1
%public
%class UnicodeCompatibilityProperties_print_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
