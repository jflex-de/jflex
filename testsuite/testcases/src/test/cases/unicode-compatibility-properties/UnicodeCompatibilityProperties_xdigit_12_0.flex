%%

%unicode 12.0
%public
%class UnicodeCompatibilityProperties_xdigit_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
