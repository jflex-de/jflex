%%

%unicode 3.0
%public
%class UnicodeMisc_Assigned_3_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
