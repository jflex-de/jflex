%%

%unicode 11.0
%public
%class UnicodeCompatibilityProperties_print_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
