%%

%unicode 5.1
%public
%class UnicodeCompatibilityProperties_print_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
