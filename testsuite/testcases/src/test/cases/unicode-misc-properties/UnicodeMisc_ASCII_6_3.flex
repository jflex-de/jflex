%%

%unicode 6.3
%public
%class UnicodeMisc_ASCII_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
