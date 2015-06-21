%%

%unicode 8.0
%public
%class UnicodeCompatibilityProperties_alnum_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
