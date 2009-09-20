%%

%unicode 3.0
%public
%class UnicodeMisc_Any_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
