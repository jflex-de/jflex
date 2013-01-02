%%

%unicode 6.2
%public
%class UnicodeCompatibilityProperties_xdigit_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
