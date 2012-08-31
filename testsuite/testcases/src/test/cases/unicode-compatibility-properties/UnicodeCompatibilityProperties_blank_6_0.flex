%%

%unicode 6.0
%public
%class UnicodeCompatibilityProperties_blank_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
