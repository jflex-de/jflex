%%

%unicode 4.0
%public
%class UnicodeCompatibilityProperties_print_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
