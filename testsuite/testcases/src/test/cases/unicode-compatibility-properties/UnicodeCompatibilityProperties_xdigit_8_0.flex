%%

%unicode 8.0
%public
%class UnicodeCompatibilityProperties_xdigit_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
