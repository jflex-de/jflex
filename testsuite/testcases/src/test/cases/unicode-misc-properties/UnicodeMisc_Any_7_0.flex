%%

%unicode 7.0
%public
%class UnicodeMisc_Any_7_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
