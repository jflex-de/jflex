%%

%unicode 9.0
%public
%class UnicodeMisc_ASCII_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
