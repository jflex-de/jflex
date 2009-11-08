%%

%unicode 5.1
%public
%class SixDigitUnicodeEscape

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\U000001 { setCurCharPropertyValue("matched"); }
[\U000003] { setCurCharPropertyValue("matched"); }
[\U000004-\U00FFFD] { setCurCharPropertyValue("matched"); }
[^\U000001\U000003-\U00FFFD] { setCurCharPropertyValue("inverse matched"); }
