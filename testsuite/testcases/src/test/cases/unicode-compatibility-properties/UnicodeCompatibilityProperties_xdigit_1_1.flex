%%

%unicode 1.1
%public
%class UnicodeCompatibilityProperties_xdigit_1_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
