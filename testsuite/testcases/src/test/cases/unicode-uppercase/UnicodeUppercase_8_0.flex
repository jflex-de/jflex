%%

%unicode 8.0
%public
%class UnicodeUppercase_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:uppercase:] { setCurCharPropertyValue("Uppercase"); }
[^[:uppercase:]] { setCurCharPropertyValue("Not-Uppercase"); }
