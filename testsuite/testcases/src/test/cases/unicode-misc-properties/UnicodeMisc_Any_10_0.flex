%%

%unicode 10.0
%public
%class UnicodeMisc_Any_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
