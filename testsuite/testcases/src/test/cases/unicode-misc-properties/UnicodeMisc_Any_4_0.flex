%%

%unicode 4.0
%public
%class UnicodeMisc_Any_4_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
