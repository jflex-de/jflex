%%

%unicode 5.1
%public
%class UnicodeCompatibilityProperties_alnum_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
