%%

%unicode 6.3
%public
%class UnicodeMisc_Any_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
