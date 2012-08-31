%%

%unicode 2.1
%public
%class UnicodeCompatibilityProperties_xdigit_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
