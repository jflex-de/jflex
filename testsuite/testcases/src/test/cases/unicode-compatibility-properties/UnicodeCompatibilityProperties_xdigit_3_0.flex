%%

%unicode 3.0
%public
%class UnicodeCompatibilityProperties_xdigit_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
