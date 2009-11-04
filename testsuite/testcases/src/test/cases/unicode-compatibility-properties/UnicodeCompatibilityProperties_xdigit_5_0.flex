%%

%unicode 5.0
%public
%class UnicodeCompatibilityProperties_xdigit_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
