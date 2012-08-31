%%

%unicode 2.0
%public
%class UnicodeMisc_ASCII_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
