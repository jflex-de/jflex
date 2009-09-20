%%

%unicode 3.1
%public
%class UnicodeDerivedCoreProperties_XID_Start_3_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{XID_Start} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
