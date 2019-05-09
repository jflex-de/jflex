%%

%unicode 12.1
%public
%class UnicodeCompatibilityProperties_alnum_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
