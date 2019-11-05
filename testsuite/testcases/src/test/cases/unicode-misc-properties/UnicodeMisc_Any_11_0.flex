%%

%unicode 11.0
%public
%class UnicodeMisc_Any_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
