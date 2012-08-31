%%

%unicode 5.2
%public
%class UnicodeCompatibilityProperties_xdigit_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{xdigit} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
