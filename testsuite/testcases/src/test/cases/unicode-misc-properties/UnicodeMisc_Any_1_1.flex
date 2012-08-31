%%

%unicode 1.1
%public
%class UnicodeMisc_Any_1_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
