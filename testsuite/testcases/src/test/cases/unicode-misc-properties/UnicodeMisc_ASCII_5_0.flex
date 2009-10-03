%%

%unicode 5.0
%public
%class UnicodeMisc_ASCII_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
