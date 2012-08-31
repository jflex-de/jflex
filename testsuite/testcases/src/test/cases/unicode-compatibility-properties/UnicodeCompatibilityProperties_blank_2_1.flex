%%

%unicode 2.1
%public
%class UnicodeCompatibilityProperties_blank_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
