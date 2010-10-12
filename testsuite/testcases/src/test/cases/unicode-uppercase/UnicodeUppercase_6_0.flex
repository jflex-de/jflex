%%

%unicode 6.0
%public
%class UnicodeUppercase_6_0

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:uppercase:] { setCurCharPropertyValue("Uppercase"); }
[^[:uppercase:]] { setCurCharPropertyValue("Not-Uppercase"); }
