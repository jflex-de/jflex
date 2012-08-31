%%

%unicode 4.0
%public
%class UnicodeCompatibilityProperties_alnum_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
