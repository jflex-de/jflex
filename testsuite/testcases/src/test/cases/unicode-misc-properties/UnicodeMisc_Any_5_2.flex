%%

%unicode 5.2
%public
%class UnicodeMisc_Any_5_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
