%%

%unicode 5.2
%public
%class UnicodeCompatibilityProperties_print_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{print} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
