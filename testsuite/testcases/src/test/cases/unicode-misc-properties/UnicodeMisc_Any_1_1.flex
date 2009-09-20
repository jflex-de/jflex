%%

%unicode 1.1
%public
%class UnicodeMisc_Any_1_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
