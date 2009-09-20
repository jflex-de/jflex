%%

%unicode 5.1
%public
%class UnicodeMisc_ASCII_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ASCII} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
