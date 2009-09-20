%%

%unicode 1.1
%public
%class UnicodeMisc_Assigned_1_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
