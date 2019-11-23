%%

%unicode 12.1
%public
%class UnicodeCompatibilityProperties_blank_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
