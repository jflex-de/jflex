%%

%unicode 3.2
%public
%class UnicodeMisc_Assigned_3_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Assigned} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
