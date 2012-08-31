%%

%unicode 3.2
%public
%class UnicodeCompatibilityProperties_alnum_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
