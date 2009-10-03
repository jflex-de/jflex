%%

%unicode 2.1
%public
%class UnicodeMisc_Assigned_2_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
