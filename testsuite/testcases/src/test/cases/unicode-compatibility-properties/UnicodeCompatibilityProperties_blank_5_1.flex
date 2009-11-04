%%

%unicode 5.1
%public
%class UnicodeCompatibilityProperties_blank_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{blank} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
