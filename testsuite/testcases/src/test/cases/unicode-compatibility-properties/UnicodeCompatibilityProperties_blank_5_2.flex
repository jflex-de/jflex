%%

%unicode 5.2
%public
%class UnicodeCompatibilityProperties_blank_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
