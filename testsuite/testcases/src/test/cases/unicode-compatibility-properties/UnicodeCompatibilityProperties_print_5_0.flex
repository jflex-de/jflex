%%

%unicode 5.0
%public
%class UnicodeCompatibilityProperties_print_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
