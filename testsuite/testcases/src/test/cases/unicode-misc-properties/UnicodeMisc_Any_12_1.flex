%%

%unicode 12.1
%public
%class UnicodeMisc_Any_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
