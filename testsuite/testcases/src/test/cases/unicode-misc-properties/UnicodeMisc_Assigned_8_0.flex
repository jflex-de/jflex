%%

%unicode 8.0
%public
%class UnicodeMisc_Assigned_8_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
