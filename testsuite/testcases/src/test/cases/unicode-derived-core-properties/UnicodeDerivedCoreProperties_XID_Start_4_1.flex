%%

%unicode 4.1
%public
%class UnicodeDerivedCoreProperties_XID_Start_4_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{XID_Start} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
