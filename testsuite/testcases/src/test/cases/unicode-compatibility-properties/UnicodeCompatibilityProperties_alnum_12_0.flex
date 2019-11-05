%%

%unicode 12.0
%public
%class UnicodeCompatibilityProperties_alnum_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
