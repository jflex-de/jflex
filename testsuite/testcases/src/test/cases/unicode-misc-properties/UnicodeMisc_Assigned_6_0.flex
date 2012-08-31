%%

%unicode 6.0
%public
%class UnicodeMisc_Assigned_6_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
