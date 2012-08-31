%%

%unicode 4.1
%public
%class UnicodeMisc_Assigned_4_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
