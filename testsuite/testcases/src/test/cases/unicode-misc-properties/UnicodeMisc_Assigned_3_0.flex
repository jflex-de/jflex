%%

%unicode 3.0
%public
%class UnicodeMisc_Assigned_3_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
