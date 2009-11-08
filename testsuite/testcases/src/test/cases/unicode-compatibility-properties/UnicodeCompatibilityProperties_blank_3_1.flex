%%

%unicode 3.1
%public
%class UnicodeCompatibilityProperties_blank_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
