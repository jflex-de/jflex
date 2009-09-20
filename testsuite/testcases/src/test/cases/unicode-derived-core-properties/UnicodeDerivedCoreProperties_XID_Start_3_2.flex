%%

%unicode 3.2
%public
%class UnicodeDerivedCoreProperties_XID_Start_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{XID_Start} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
