%%

%unicode 5.2
%public
%class UnicodeLetter_5_2

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:letter:] { setCurCharPropertyValue("Letter"); }
[^[:letter:]] { setCurCharPropertyValue("Not-Letter"); }
