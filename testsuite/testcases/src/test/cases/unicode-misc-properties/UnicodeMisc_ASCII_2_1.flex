%%

%unicode 2.1
%public
%class UnicodeMisc_ASCII_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
