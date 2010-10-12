%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_XID_Start_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{XID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
