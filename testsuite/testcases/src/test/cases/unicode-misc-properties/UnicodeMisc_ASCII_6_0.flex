%%

%unicode 6.0
%public
%class UnicodeMisc_ASCII_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
