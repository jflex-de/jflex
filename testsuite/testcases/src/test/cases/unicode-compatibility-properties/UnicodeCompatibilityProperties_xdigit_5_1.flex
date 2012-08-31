%%

%unicode 5.1
%public
%class UnicodeCompatibilityProperties_xdigit_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
