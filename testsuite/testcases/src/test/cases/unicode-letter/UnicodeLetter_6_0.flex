%%

%unicode 6.0
%public
%class UnicodeLetter_6_0

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:letter:] { setCurCharPropertyValue("Letter"); }
[^[:letter:]] { setCurCharPropertyValue("Not-Letter"); }
