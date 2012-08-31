%%

%unicode 6.1
%public
%class UnicodeCompatibilityProperties_xdigit_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
