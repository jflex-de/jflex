%%

%unicode 5.2
%public
%class UnicodeCompatibilityProperties_alnum_5_2

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{alnum} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
