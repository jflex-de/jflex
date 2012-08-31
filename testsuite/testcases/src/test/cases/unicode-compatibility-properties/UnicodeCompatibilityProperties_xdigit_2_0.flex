%%

%unicode 2.0
%public
%class UnicodeCompatibilityProperties_xdigit_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
