%%

%unicode 1.1
%public
%class UnicodeMisc_ASCII_1_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
