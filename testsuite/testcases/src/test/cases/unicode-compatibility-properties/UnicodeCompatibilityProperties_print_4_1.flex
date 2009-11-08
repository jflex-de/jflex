%%

%unicode 4.1
%public
%class UnicodeCompatibilityProperties_print_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
