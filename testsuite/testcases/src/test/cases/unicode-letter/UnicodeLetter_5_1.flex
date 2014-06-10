%%

%unicode 5.1
%public
%class UnicodeLetter_5_1

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
[:letter:] { setCurCharPropertyValue("Letter"); }
[^[:letter:]] { setCurCharPropertyValue("Not-Letter"); }
