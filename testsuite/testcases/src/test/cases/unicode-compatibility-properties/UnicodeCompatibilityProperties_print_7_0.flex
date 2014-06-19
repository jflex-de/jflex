%%

%unicode 7.0
%public
%class UnicodeCompatibilityProperties_print_7_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
