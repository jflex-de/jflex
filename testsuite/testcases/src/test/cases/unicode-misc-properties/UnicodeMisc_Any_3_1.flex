%%

%unicode 3.1
%public
%class UnicodeMisc_Any_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{Any} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
