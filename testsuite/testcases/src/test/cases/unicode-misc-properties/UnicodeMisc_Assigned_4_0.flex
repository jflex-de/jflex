%%

%unicode 4.0
%public
%class UnicodeMisc_Assigned_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
