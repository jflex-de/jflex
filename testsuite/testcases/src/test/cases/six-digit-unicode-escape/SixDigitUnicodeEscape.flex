%%

%unicode
%public
%class SixDigitUnicodeEscape

%type int
%standalone
%buffer 300 

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\U000001 { setCurCharPropertyValue("matched"); }
[\U000003] { setCurCharPropertyValue("matched"); }
"\U000004" { setCurCharPropertyValue("matched"); }
[\U000005-\U10FFFD] { setCurCharPropertyValue("matched"); }
[^\U000001\U000003-\U10FFFD] { setCurCharPropertyValue("inverse matched"); }
