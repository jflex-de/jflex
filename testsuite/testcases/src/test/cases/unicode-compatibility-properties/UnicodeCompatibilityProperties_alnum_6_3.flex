%%

%unicode 6.3
%public
%class UnicodeCompatibilityProperties_alnum_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
