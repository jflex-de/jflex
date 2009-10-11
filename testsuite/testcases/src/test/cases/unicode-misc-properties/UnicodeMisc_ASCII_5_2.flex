%%

%unicode 5.2
%public
%class UnicodeMisc_ASCII_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
