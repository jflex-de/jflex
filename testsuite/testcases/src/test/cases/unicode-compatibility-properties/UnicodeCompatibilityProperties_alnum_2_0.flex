%%

%unicode 2.0
%public
%class UnicodeCompatibilityProperties_alnum_2_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
