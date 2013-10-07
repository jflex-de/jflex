%%

%unicode 6.3
%public
%class UnicodeMisc_Assigned_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
