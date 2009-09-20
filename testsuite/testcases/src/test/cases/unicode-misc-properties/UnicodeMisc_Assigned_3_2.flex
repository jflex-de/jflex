%%

%unicode 3.2
%public
%class UnicodeMisc_Assigned_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
