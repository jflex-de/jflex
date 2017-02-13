%%

%unicode 9.0
%public
%class UnicodeMisc_Any_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
