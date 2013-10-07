%%

%unicode 6.3
%public
%class UnicodeLetter_6_3

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:letter:] { setCurCharPropertyValue("Letter"); }
[^[:letter:]] { setCurCharPropertyValue("Not-Letter"); }
