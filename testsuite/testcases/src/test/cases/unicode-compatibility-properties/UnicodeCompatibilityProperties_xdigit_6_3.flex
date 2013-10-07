%%

%unicode 6.3
%public
%class UnicodeCompatibilityProperties_xdigit_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
