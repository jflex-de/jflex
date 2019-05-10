%%

%unicode 12.1
%public
%class UnicodeCompatibilityProperties_xdigit_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
