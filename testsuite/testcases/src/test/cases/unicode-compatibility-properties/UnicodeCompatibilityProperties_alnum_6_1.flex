%%

%unicode 6.1
%public
%class UnicodeCompatibilityProperties_alnum_6_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
