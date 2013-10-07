%%

%unicode 6.3
%public
%class UnicodeCompatibilityProperties_blank_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
