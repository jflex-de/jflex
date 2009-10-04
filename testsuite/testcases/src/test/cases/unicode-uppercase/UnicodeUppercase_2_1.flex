%%

%unicode 2.1
%public
%class UnicodeUppercase_2_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:uppercase:] { setCurCharPropertyValue("Uppercase"); }
[^[:uppercase:]] { setCurCharPropertyValue("Not-Uppercase"); }
