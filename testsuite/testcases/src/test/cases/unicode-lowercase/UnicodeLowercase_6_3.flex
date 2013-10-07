%%

%unicode 6.3
%public
%class UnicodeLowercase_6_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:lowercase:] { setCurCharPropertyValue("Lowercase"); }
[^[:lowercase:]] { setCurCharPropertyValue("Not-Lowercase"); }
