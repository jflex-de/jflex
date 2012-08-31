%%

%unicode 3.2
%public
%class UnicodeCompatibilityProperties_blank_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
