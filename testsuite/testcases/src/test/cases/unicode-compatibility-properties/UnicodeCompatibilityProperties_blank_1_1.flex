%%

%unicode 1.1
%public
%class UnicodeCompatibilityProperties_blank_1_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
