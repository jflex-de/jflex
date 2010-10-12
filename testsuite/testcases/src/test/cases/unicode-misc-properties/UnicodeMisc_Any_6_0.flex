%%

%unicode 6.0
%public
%class UnicodeMisc_Any_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
